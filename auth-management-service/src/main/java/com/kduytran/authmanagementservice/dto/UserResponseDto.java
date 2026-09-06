package com.kduytran.authmanagementservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponseDto {
    private String id;
    private String username;
    private String givenName;
    private String familyName;
    private String name;
    private Boolean emailVerified;
    private List<String> roles;
    private String email;
}
