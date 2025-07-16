package com.kduytran.authmanagementservice.controller;

import com.kduytran.authmanagementservice.dto.UserDTO;
import com.kduytran.authmanagementservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
@RequiredArgsConstructor
class UserController {

    private final UserService userService;

    @GetMapping("/api/my-info")
    UserDTO getMyInfo(@CookieValue("accessToken") String token) {
        return userService.getUserFromAccessToken(token);
    }
}
