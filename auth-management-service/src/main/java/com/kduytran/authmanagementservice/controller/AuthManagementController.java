package com.kduytran.authmanagementservice.controller;

import com.kduytran.authmanagementservice.dto.UserDTO;
import com.kduytran.authmanagementservice.service.IAuthManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth-management")
@RequiredArgsConstructor
public class AuthManagementController {
    private final IAuthManagementService authManagementService;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserByUsername(@RequestParam String username) {
        return authManagementService.getUserByUsername(username);
    }

}
