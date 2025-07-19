package com.kduytran.authmanagementservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDTO {
    private String sub;
    private String name;
    private String picture;
    private String username;
    private List<String> roles;
    private List<String> permissions;
}
