package com.kduytran.authmanagementservice.dto;

import lombok.Data;

@Data
public class UserRegisterRequestDTO {
    private String userName;
    private String email;
    private String phoneNumber;
    private String password;
    private String firstname;
    private String lastName;
}
