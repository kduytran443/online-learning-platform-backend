package com.kduytran.authmanagementservice.service;

import com.kduytran.authmanagementservice.dto.UserDTO;

public interface UserService {
    UserDTO getUserFromAccessToken(String token);
}
