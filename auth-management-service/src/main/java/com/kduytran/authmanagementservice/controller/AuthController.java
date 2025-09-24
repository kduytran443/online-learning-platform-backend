package com.kduytran.authmanagementservice.controller;

import com.kduytran.authmanagementservice.constant.ResponseConstant;
import com.kduytran.authmanagementservice.dto.RegistrationDTO;
import com.kduytran.authmanagementservice.dto.ResponseDTO;
import com.kduytran.authmanagementservice.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.kduytran.authmanagementservice.constant.ApiPathConstant.*;

@RestController
@RequestMapping(
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
@RequiredArgsConstructor
class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(V1_AUTH_REGISTRATION)
    ResponseDTO registerUser(@RequestBody RegistrationDTO registrationDTO) {
        authService.signup(registrationDTO);
        return ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(V1_AUTH_VERIFICATION)
    ResponseDTO verifyUser(@PathParam("token") String token) {
        authService.verifyUserRegistration(token);
        return ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(V1_AUTH_REFRESH_VERIFICATION)
    ResponseDTO refreshUserVerification(@PathParam("username") String username) {
        authService.refreshUserVerification(username);
        return ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(V1_LOGIN_SUCCESS)
    void loginSuccess(HttpServletResponse response,
                      @Value("${olp.frontend-url}") String frontEndUrl,
                      @Value("${olp.frontend-login-success-path}") String frontendLoginSuccessPath) throws IOException {
        response.sendRedirect("%s/%s".formatted(frontEndUrl, frontendLoginSuccessPath));
    }
}
