package com.kduytran.authmanagementservice.controller;

import com.kduytran.authmanagementservice.constant.ResponseConstant;
import com.kduytran.authmanagementservice.dto.ResponseDTO;
import com.kduytran.authmanagementservice.dto.UserDTO;
import com.kduytran.authmanagementservice.dto.UserRequestDTO;
import com.kduytran.authmanagementservice.service.IAuthManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth-management")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthManagementController {
    private final IAuthManagementService authManagementService;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserByUsername(@RequestParam String username) {
        return authManagementService.getUserByUsername(username);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO registerUser(@RequestBody @Valid UserRequestDTO dto) {
        log.info("Registering user with request: userRequestDTO={}", dto);
        authManagementService.registerUser(dto);
        return ResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.MESSAGE_201);
    }

    @PutMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO updateUser(@RequestBody @Valid UserRequestDTO dto) {
        log.info("Updating user with request: userRequestDTO={}", dto);
        authManagementService.updateUser(dto);
        return ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200);
    }

    @DeleteMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO removeUser(@RequestParam String userId) {
        log.info("Removing user with request: userId={}", userId);
        authManagementService.removeUser(userId);
        return ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200);
    }

}
