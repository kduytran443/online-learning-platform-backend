package com.kduytran.authmanagementservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class AddScopeDTO {
    private String userId;
    private String resourceId;
    private Set<String> scopes;
}
