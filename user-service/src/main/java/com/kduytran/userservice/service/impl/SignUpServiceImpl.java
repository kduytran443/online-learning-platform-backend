package com.kduytran.userservice.service.impl;

import com.kduytran.userservice.dto.RegistrationDTO;
import com.kduytran.userservice.entity.SignUpEntity;
import com.kduytran.userservice.entity.UserVerificationEntity;
import com.kduytran.userservice.event.EventType;
import com.kduytran.userservice.event.UserRegisteredEvent;
import com.kduytran.userservice.exception.UserVerificationNotFoundException;
import com.kduytran.userservice.exception.VerificationTokenExpiredException;
import com.kduytran.userservice.mapper.SignUpMapper;
import com.kduytran.userservice.repository.SignUpRepository;
import com.kduytran.userservice.repository.UserVerificationRepository;
import com.kduytran.userservice.service.ISignUpService;
import com.kduytran.userservice.service.IUserService;
import com.kduytran.userservice.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SignUpServiceImpl implements ISignUpService {

    private final SignUpRepository signUpRepository;
    private final UserVerificationRepository userVerificationRepository;
    private final long expiredTime;
    private final IUserService userService;
    private final ApplicationEventPublisher publisher;

    public SignUpServiceImpl(SignUpRepository signUpRepository,
                             UserVerificationRepository userVerificationRepository,
                             @Value("${verification.expired-time}") String expiredTime, IUserService userService,
                             ApplicationEventPublisher publisher) {
        this.signUpRepository = signUpRepository;
        this.userVerificationRepository = userVerificationRepository;
        this.expiredTime = Long.parseLong(expiredTime);
        this.userService = userService;
        this.publisher = publisher;
    }

    @Override
    @Transactional
    public void signUp(String correlationId, RegistrationDTO registrationDTO) {
        SignUpEntity signUpEntity = SignUpMapper.convert(registrationDTO, new SignUpEntity());
        signUpEntity.setCreatedAt(LocalDateTime.now());

        UserVerificationEntity verification = createNewToken(signUpEntity);

        signUpEntity.setUserVerifications(List.of(verification));
        signUpEntity = signUpRepository.save(signUpEntity);

        publishRegisteredUser(signUpEntity, correlationId);
    }

    @Override
    @Transactional
    public void verifyUserRegistration(String correlationId, String token) {
        UserVerificationEntity userVerification = userVerificationRepository.findById(token).orElseThrow(
                () -> new UserVerificationNotFoundException("Verification details for the user were not found.")
        );

        if (TimeUtils.isExpired(userVerification.getExpiredDate())) {
            throw new VerificationTokenExpiredException("Verification token has expired. Please request a new token " +
                    "to continue.");
        }

        userVerification.setChecked(true);
        this.userVerificationRepository.save(userVerification);

        userService.createUser(userVerification.getSignUp());
    }

    @Override
    @Transactional
    public void refreshUserVerification(String correlationId, String userId) {

    }

    private UserVerificationEntity createNewToken(SignUpEntity signUpEntity) {
        UserVerificationEntity userVerification = new UserVerificationEntity();
        userVerification.setToken(UUID.randomUUID().toString());
        userVerification.setSignUp(signUpEntity);
        userVerification.setExpiredDate(TimeUtils.getExpiredTime(expiredTime));
        userVerification.setChecked(false);

        return userVerification;
    }

    private void publishRegisteredUser(SignUpEntity entity, String correlationId) {
        UserVerificationEntity userVerification = entity.getUserVerifications().get(0);
        UserRegisteredEvent event = new UserRegisteredEvent(
                correlationId,
                EventType.CREATED,
                entity.getId().toString(),
                entity.getUsername(),
                entity.getName(),
                entity.getEmail(),
                entity.getUserType().getCode(),
                userVerification.getToken(),
                userVerification.getExpiredDate().toString(),
                entity.getCreatedAt().toString()
        );
        publisher.publishEvent(event);
    }

}
