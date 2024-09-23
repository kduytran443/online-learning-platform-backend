package com.kduytran.authmanagementservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ScopePermissionDTO {
    private String userId;
    private Set<String> scopes;
    private Set<String> resources;
    private String name;
    private String description;
}
