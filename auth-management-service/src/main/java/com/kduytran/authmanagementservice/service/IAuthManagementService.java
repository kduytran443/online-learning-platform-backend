package com.kduytran.authmanagementservice.service;

import com.kduytran.authmanagementservice.dto.UserDTO;
import com.kduytran.authmanagementservice.dto.UserRequestDTO;

public interface IAuthManagementService {
    void removeUser(String userId);
    void updateUser(UserRequestDTO dto);
    void registerUser(UserRequestDTO dto);
    UserDTO getUserByUsername(String username);
}
