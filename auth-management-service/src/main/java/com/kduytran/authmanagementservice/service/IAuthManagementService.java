package com.kduytran.authmanagementservice.service;

import com.kduytran.authmanagementservice.dto.UserDTO;
import com.kduytran.authmanagementservice.dto.UserRequestDTO;

public interface IAuthManagementService {
    boolean removeUser(String userId);
    void updateUser(UserRequestDTO dto);
    int registerUser(UserRequestDTO dto);
    UserDTO getUserByUsername(String username);
}
