package com.kduytran.authmanagementservice.service;

import com.kduytran.authmanagementservice.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto getById(String id, boolean attachRoles);
    UserResponseDto getByUsername(String username, boolean attachRoles);
    List<UserResponseDto> getAllByUsername(List<String> usernames);
}
