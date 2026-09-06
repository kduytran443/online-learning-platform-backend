package com.kduytran.authmanagementservice.controller;

import com.kduytran.authmanagementservice.dto.UserResponseDto;
import com.kduytran.authmanagementservice.service.UserService;
import com.kduytran.olpcommon.auth.annotation.HasRole;
import com.kduytran.olpcommon.auth.annotation.HasRoleOrScope;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/users/me")
    @HasRole("user")
    UserResponseDto getMe(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("attachRoles") boolean attachRoles
    ) {
        return userService.getById(jwt.getSubject(), attachRoles);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/users")
    @HasRoleOrScope(role = "admin", scope = "user:retrieve")
    UserResponseDto getByUsername(
            @RequestParam("username") String username,
            @RequestParam(value = "attachRoles", defaultValue = "false") boolean attachRoles
    ) {
        return userService.getByUsername(username, attachRoles);
    }
}
