package com.kduytran.authmanagementservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResourceRequestDTO {
    @NotEmpty
    private String documentId;

    @NotEmpty
    private String userId;

    @NotEmpty
    private String type;

    @NotEmpty
    private String uri;
}
