package com.kduytran.userservice.service.impl;

import com.kduytran.userservice.dto.UserDTO;
import com.kduytran.userservice.entity.SignUpEntity;
import com.kduytran.userservice.entity.UserEntity;
import com.kduytran.userservice.entity.UserStatus;
import com.kduytran.userservice.exception.*;
import com.kduytran.userservice.mapper.UserMapper;
import com.kduytran.userservice.repository.UserRepository;
import com.kduytran.userservice.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void createUser(SignUpEntity signUpEntity) {
        if (userRepository.existsByUsername(signUpEntity.getUsername())) {
            throw new UserAlreadyExistsException("User is already existed");
        }
        if (userRepository.existsByEmail(signUpEntity.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already existed");
        }
        if (userRepository.existsByMobilePhone(signUpEntity.getMobilePhone())) {
            throw new MobilePhoneAlreadyExistsException("Mobile phone is already existed");
        }

        // Save user info to database
        UserEntity userEntity = UserMapper.convert(signUpEntity, new UserEntity());
        userEntity.setUserStatus(UserStatus.ACTIVE);
        userRepository.save(userEntity);
    }

    @Override
    public UserDTO fetchUser(String id) {
        UserEntity user = userRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );
        return UserMapper.convert(user, new UserDTO());
    }

    @Override
    public void changeUserStatus(UUID userId, UserStatus userStatus) {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId.toString())
        );
        user.setUserStatus(userStatus);
        this.userRepository.save(user);
    }

    @Override
    public int deleteAllInActiveUsers() {
        return this.userRepository.deleteAllInActiveUsers();
    }


}
