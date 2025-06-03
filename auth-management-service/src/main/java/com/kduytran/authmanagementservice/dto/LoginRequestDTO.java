package com.kduytran.authmanagementservice.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank String username,
        @NotBlank String password) {
}
