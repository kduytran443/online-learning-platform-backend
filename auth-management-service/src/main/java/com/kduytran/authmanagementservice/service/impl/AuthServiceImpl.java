package com.kduytran.authmanagementservice.service.impl;

import com.kduytran.authmanagementservice.dto.RegistrationDTO;
import com.kduytran.authmanagementservice.entity.SignUpEntity;
import com.kduytran.authmanagementservice.entity.SignUpStatus;
import com.kduytran.authmanagementservice.exception.SignUpNotValidException;
import com.kduytran.authmanagementservice.mapper.RegistrationMapper;
import com.kduytran.authmanagementservice.repository.SignUpRepository;
import com.kduytran.authmanagementservice.service.AuthService;
import com.kduytran.authmanagementservice.utils.TimeUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RegistrationMapper registrationMapper;
    private final SignUpRepository signUpRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void login(String username, String password) {

    }

    @Override
    public void logout() {

    }

    @Override
    public void signup(RegistrationDTO registrationDTO) {
        checkExistingSignUpUser(registrationDTO);
        SignUpEntity signUpEntity = registrationMapper.map(registrationDTO, new SignUpEntity());
        signUpEntity.setCreatedAt(LocalDateTime.now());
        signUpEntity.setStatus(SignUpStatus.PENDING);
        signUpEntity.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        assignNewToken(signUpEntity);

        signUpRepository.save(signUpEntity);
    }

    @Override
    public void verifyUserRegistration(String token) {
        SignUpEntity signUpEntity = signUpRepository.findByCurrentVerificationTokenAndStatusIn(token,
                        List.of(SignUpStatus.PENDING))
                .orElseThrow(() -> new SignUpNotValidException("Token is not valid: " + token));
        if (signUpEntity.getExpiredVerificationTokenDate() == null) {
            throw new SignUpNotValidException("Token is not valid: " + token);
        }
        if (LocalDateTime.now().isAfter(signUpEntity.getExpiredVerificationTokenDate())) {
            throw new SignUpNotValidException("Token is expired: " + token);
        }
        signUpEntity.setStatus(SignUpStatus.SUCCESS);
        signUpRepository.save(signUpEntity);
    }

    @Override
    public void refreshUserVerification(String username) {
        SignUpEntity signUpEntity = signUpRepository.findByUsernameAndStatusIn(username,
                        List.of(SignUpStatus.PENDING))
                .orElseThrow(() -> new SignUpNotValidException("Username is not found: " + username));
        assignNewToken(signUpEntity);
        signUpRepository.save(signUpEntity);
    }

    private void checkExistingSignUpUser(RegistrationDTO dto) {
        List<SignUpStatus> validStatuses = List.of(SignUpStatus.PENDING, SignUpStatus.SUCCESS);

        if (signUpRepository.existsByUsernameAndStatusIn(dto.getUsername(), validStatuses)) {
            throw new SignUpNotValidException("Username already exists");
        }

        if (signUpRepository.existsByEmailAndStatusIn(dto.getEmail(), validStatuses)) {
            throw new SignUpNotValidException("Email already registered");
        }

        if (signUpRepository.existsByMobilePhoneAndStatusIn(dto.getMobilePhone(), validStatuses)) {
            throw new SignUpNotValidException("Phone number already used");
        }
    }

    private void assignNewToken(SignUpEntity signUpEntity) {
        final String token = UUID.randomUUID().toString();
        signUpEntity.setCurrentVerificationToken(token);
        signUpEntity.setExpiredVerificationTokenDate(TimeUtils.getExpiredTime(300)); // 300s
        log.debug("Assign new token {} for user {}", token, signUpEntity.getName());
    }
}
