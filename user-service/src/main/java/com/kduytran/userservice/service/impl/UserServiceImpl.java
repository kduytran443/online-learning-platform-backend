package com.kduytran.userservice.service.impl;

import com.kduytran.userservice.dto.RegistrationDTO;
import com.kduytran.userservice.dto.UserDTO;
import com.kduytran.userservice.entity.UserEntity;
import com.kduytran.userservice.entity.UserStatus;
import com.kduytran.userservice.entity.UserVerificationEntity;
import com.kduytran.userservice.event.UserRegisteredEvent;
import com.kduytran.userservice.exception.*;
import com.kduytran.userservice.mapper.UserMapper;
import com.kduytran.userservice.repository.UserRepository;
import com.kduytran.userservice.repository.UserVerificationRepository;
import com.kduytran.userservice.service.IUserService;
import com.kduytran.userservice.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final String expiredTime;
    private final UserRepository userRepository;
    private final UserVerificationRepository userVerificationRepository;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public UserServiceImpl(@Value("${verification.expired-time}") String expiredTime,
                           UserRepository userRepository,
                           UserVerificationRepository userVerificationRepository, ApplicationEventPublisher publisher) {
        this.expiredTime = expiredTime;
        this.userRepository = userRepository;
        this.userVerificationRepository = userVerificationRepository;
        this.publisher = publisher;
    }

    /**
     * Creates a new user based on the information from the provided RegistrationDTO object.
     *
     * @param registrationDTO The registration information of the user, containing necessary details to create the user.
     * @return true if the user has been successfully created, false if an error occurred during user creation.
     * @throws UserAlreadyExistsException If the user already exists in the system.
     * @throws EmailAlreadyExistsException If the email already exists in the system.
     * @throws MobilePhoneAlreadyExistsException If the user already exists in the system.
     */
    @Override
    @Transactional
    public void createUser(RegistrationDTO registrationDTO) {
        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new UserAlreadyExistsException("User is already existed");
        }
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already existed");
        }
        if (userRepository.existsByMobilePhone(registrationDTO.getMobilePhone())) {
            throw new MobilePhoneAlreadyExistsException("Mobile phone is already existed");
        }

        // Save user info to database
        UserEntity userEntity = UserMapper.convert(registrationDTO, new UserEntity());
        userEntity.setUserStatus(UserStatus.INACTIVE);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        // TODO - Add token for user
        UserVerificationEntity userVerification = createNewToken(userEntity);
        UserVerificationEntity savedUserVerification = this.userVerificationRepository.save(userVerification);

        publishRegisteredUser(savedUserEntity, savedUserVerification);
    }

    /**
     * This method is used to fetch user information based on the given ID.
     *
     * @param id The ID of the user to retrieve.
     * @return A UserDTO object containing the user information corresponding to the given ID.
     *         If no user is found with the provided ID, the method will return null.
     * @throws IllegalArgumentException If the input (ID) is invalid, such as being null or empty.
     * @throws UserNotFoundException   If no user is found with the corresponding ID in the system.
     */
    @Override
    public UserDTO fetchUser(String id) {
        UserEntity user = userRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );
        return UserMapper.convert(user, new UserDTO());
    }

    /**
     * Updates the status of a user based on the given user ID and the new status.
     *
     * @param userId     the unique identifier of the user whose status will be changed.
     * @param userStatus the new status to assign to the user, typically an enum representing various states.
     */
    @Override
    public void changeUserStatus(UUID userId, UserStatus userStatus) {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId.toString())
        );
        user.setUserStatus(userStatus);
        this.userRepository.save(user);
    }

    /**
     * Verifies a user's registration using a given token.
     *
     * @param token the verification token sent to the user via email.
     * @return true if the token is valid and the user is successfully verified,
     * false if the token is invalid or verification fails.
     */
    @Override
    @Transactional
    public void verifyUserRegistration(String token) {
        UserVerificationEntity userVerification = userVerificationRepository.findById(token).orElseThrow(
                () -> new UserVerificationNotFoundException("Verification details for the user were not found.")
        );

        if (TimeUtils.isExpired(userVerification.getExpiredDate())) {
            throw new VerificationTokenExpiredException("Verification token has expired. Please request a new token to continue.");
        }

        userVerification.setChecked(true);
        this.userVerificationRepository.save(userVerification);
        this.changeUserStatus(userVerification.getUser().getId(), UserStatus.ACTIVE);
    }

    /**
     * Resends the email verification for a given user.
     *
     * <p>This method is typically used when a user needs to receive a new email verification
     * during the registration process. It sends a new verification email to the user's registered
     * email address based on the provided user ID. This can be useful if the user did not receive
     * the initial verification email or if it expired.</p>
     *
     * @param userId The ID of the user who needs a new email verification. This should be a non-null and non-empty string.
     *
     * @throws IllegalArgumentException If the userId is null or empty.
     * @throws com.kduytran.userservice.exception.UserNotFoundException If no user is found with the specified userId.
     */
    @Override
    @Transactional
    public void refreshUserVerification(String userId) {
        UserEntity userEntity = userRepository.findByUserStatusAndId(UserStatus.INACTIVE, UUID.fromString(userId)).orElseThrow(
                () -> new UserNotFoundException("User with the given ID does not exist or is not in INACTIVE status.")
        );
        UserVerificationEntity userVerification = createNewToken(userEntity);
        UserVerificationEntity savedUserVerification = userVerificationRepository.save(userVerification);
        publishRegisteredUser(userEntity, savedUserVerification);
    }

    /**
     * Deletes all inactive users from the system.
     * An inactive user is defined as a user who has not logged in for a specified period or
     * has a status indicating they are no longer active.
     *
     * @return the number of users that were deleted from the system.
     */
    @Override
    public int deleteAllInActiveUsers() {
        return this.userRepository.deleteAllInActiveUsers();
    }

    private void publishRegisteredUser(UserEntity userEntity, UserVerificationEntity userVerification) {
        UserRegisteredEvent event = UserRegisteredEvent.of(
                "", // TODO - ADD TRANSACTION ID
                userEntity.getUsername(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getUserType().getCode(),
                userVerification.getToken(),
                userVerification.getExpiredDate().toString(),
                userEntity.getCreatedAt().toString()
        );

        logger.debug("Publishing user registered event {}", event);
        publisher.publishEvent(event);
    }

    private UserVerificationEntity createNewToken(UserEntity userEntity) {
        UserVerificationEntity userVerification = new UserVerificationEntity();
        userVerification.setToken(UUID.randomUUID().toString());
        userVerification.setUser(userEntity);
        userVerification.setExpiredDate(TimeUtils.getExpiredTime(Long.parseLong(expiredTime)));
        userVerification.setChecked(false);
        return userVerification;
    }

}
