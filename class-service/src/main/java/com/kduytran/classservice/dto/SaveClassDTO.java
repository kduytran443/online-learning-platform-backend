package com.kduytran.classservice.dto;

import com.kduytran.classservice.entity.Accessibility;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class SaveClassDTO {

    @Schema(description = "Name of the class")
    @NotBlank(message = "Name can not be null or empty")
    private String name;

    @NotBlank(message = "Accessibility can not be null or empty")
    @Pattern(regexp = "^(PUBLIC|LINK_ONLY|PAID|PRIVATE)$", message = "Accessibility is not valid")
    private Accessibility accessibility;

    @NotBlank(message = "Category ID can not be null or empty")
    private UUID categoryId;

    @Schema(
            description = "Information about the Class Owner",
            example = "This is the description of the class owner."
    )
    @NotEmpty(message = "Class owner ID cannot be null or empty")
    private UUID ownerId;
}
