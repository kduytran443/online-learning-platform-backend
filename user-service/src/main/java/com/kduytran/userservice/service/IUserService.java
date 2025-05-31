package com.kduytran.userservice.service;

import com.kduytran.userservice.dto.UserDTO;
import com.kduytran.userservice.entity.SignUpEntity;
import com.kduytran.userservice.entity.UserStatus;

import java.util.UUID;

public interface IUserService {

    void createUser(SignUpEntity signUpEntity);

    UserDTO fetchUser(String id);

    void changeUserStatus(UUID userId, UserStatus userStatus);


    int deleteAllInActiveUsers();

}
