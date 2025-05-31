package com.kduytran.authmanagementservice.service;

import com.kduytran.authmanagementservice.dto.RegistrationDTO;

public interface AuthService {

    void login(String username, String password);

    void logout();

    void signup(RegistrationDTO registrationDTO);

    void verifyUserRegistration(String token);

    void refreshUserVerification(String username);
}
