package com.kduytran.authmanagementservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RemoveAllScopesDTO {

    @NotEmpty(message = "userId can not be null or empty")
    private String userId;

    @NotEmpty(message = "resourceId can not be null or empty")
    private String resourceId;

}
