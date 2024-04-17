package com.kduytran.userservice.service.impl;

import com.kduytran.userservice.dto.RegistrationDTO;
import com.kduytran.userservice.dto.UserDTO;
import com.kduytran.userservice.entity.UserEntity;
import com.kduytran.userservice.entity.UserStatus;
import com.kduytran.userservice.exception.EmailAlreadyExistsException;
import com.kduytran.userservice.exception.MobilePhoneAlreadyExistsException;
import com.kduytran.userservice.exception.ResourceNotFoundException;
import com.kduytran.userservice.mapper.UserMapper;
import com.kduytran.userservice.repository.UserRepository;
import com.kduytran.userservice.service.IUserService;
import com.kduytran.userservice.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        // TODO - invoke KeyCloak API to create user

        // Save user info to database
        UserEntity userEntity = UserMapper.convert(registrationDTO, new UserEntity());
        userEntity.setUserStatus(UserStatus.INACTIVE);
        userRepository.save(userEntity);
    }

    /**
     * Fetches user information based on the provided username.
     *
     * @param username The username of the user to fetch information for.
     * @return A UserDTO object containing information about the user.
     * @throws com.kduytran.userservice.exception.UserNotFoundException if the user with the specified username is not found.
     */
    @Override
    public UserDTO fetchUser(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username)
        );
        return UserMapper.convert(user, new UserDTO());
    }

}
