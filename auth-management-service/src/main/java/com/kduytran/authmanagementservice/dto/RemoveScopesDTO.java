package com.kduytran.authmanagementservice.dto;

import com.kduytran.authmanagementservice.validator.ValidScopes;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class RemoveScopesDTO {

    @NotEmpty(message = "userId can not be null or empty")
    private String userId;

    @NotEmpty(message = "resourceId can not be null or empty")
    private String resourceId;

    @ValidScopes
    private Set<String> scopes;

}
