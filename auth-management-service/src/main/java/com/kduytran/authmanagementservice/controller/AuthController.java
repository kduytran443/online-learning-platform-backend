package com.kduytran.authmanagementservice.controller;

import com.kduytran.authmanagementservice.constant.ResponseConstant;
import com.kduytran.authmanagementservice.dto.RegistrationDTO;
import com.kduytran.authmanagementservice.dto.ResponseDTO;
import com.kduytran.authmanagementservice.service.AuthService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping(
        path = "/api/v1/auth",
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
@RequiredArgsConstructor
class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/registration")
    ResponseDTO registerUser(@RequestBody RegistrationDTO registrationDTO) {
        authService.signup(registrationDTO);
        return ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/verification")
    ResponseDTO verifyUser(@PathParam("token") String token) {
        authService.verifyUserRegistration(token);
        return ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/refresh-verification")
    ResponseDTO refreshUserVerification(@PathParam("username") String username) {
        authService.refreshUserVerification(username);
        return ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200);
    }
}
