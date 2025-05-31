package com.kduytran.userservice.service;

import com.kduytran.userservice.dto.RegistrationDTO;

public interface ISignUpService {

    void signUp(String correlationId, RegistrationDTO registrationDTO);

    void verifyUserRegistration(String correlationId, String token);

    void refreshUserVerification(String correlationId, String userId);

}
