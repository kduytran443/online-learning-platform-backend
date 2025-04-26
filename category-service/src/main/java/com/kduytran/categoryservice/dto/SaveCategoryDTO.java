package com.kduytran.categoryservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(
        name = "CreateCategoryDTO",
        description = "Schema to hold information to save Category"
)
public class SaveCategoryDTO {

    private String id;

    @NotEmpty(message = "Name can not be null or empty")
    private String name;

    @NotEmpty(message = "Description can not be null or empty")
    private String description;

    @NotEmpty(message = "Code can not be null or empty")
    @Pattern(regexp = "^([a-z0-9](-[a-z0-9]+)*)+$", message = "Code is not in valid format")
    private String code;

    private String parentCategoryId;
}
