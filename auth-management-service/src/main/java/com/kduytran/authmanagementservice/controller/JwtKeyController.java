package com.kduytran.authmanagementservice.controller;

import com.kduytran.authmanagementservice.service.JwtKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(
        path = "/api/v1/jwt-key",
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
@RequiredArgsConstructor
class JwtKeyController {

    private final JwtKeyService jwtKeyService;

    @GetMapping("/public-key")
    ResponseEntity<String> getPublicKeyInBase64() {
        return ResponseEntity.ok(jwtKeyService.getPublicKeyInBase64());
    }
}
