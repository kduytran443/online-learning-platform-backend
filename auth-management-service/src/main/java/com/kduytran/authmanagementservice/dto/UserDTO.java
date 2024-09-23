package com.kduytran.authmanagementservice.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String userName;
    private String email;
    private String fullName;
}
