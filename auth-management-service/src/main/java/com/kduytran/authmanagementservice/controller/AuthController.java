package com.kduytran.authmanagementservice.controller;

import com.kduytran.authmanagementservice.dto.LoginRequestDTO;
import com.kduytran.authmanagementservice.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(
        path = "/api/v1/auth",
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
@RequiredArgsConstructor
class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    AccessTokenResponse login(@Valid @RequestBody LoginRequestDTO dto) {
        return authService.login(dto.username(), dto.password());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout")
    void logout(@NotBlank @RequestParam String refreshToken) {
        authService.logout(refreshToken);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/refresh-token")
    AccessTokenResponse refreshToken(@NotBlank @RequestParam String refreshToken) {
        return authService.refreshToken(refreshToken);
    }
}
