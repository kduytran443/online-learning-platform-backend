package com.kduytran.authmanagementservice.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserRequestDTO {
    private String id;
    private String userName;
    private String email;
    private String phoneNumber;
    private String password;
    private String fullName;
}
