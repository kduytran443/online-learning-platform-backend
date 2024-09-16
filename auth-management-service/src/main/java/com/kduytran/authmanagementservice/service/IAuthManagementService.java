package com.kduytran.authmanagementservice.service;

import com.kduytran.authmanagementservice.dto.UserDTO;
import com.kduytran.authmanagementservice.dto.UserRegisterRequestDTO;

public interface IAuthManagementService {

    boolean registerUser(UserRegisterRequestDTO dto);
    UserDTO getUserByUsername(String username);

}
