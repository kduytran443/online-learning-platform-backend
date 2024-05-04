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
        description = "Schema to hold information to create Category"
)
public class CreateCategoryDTO {

    @Schema(
            description = "Name of category", example = "Java"
    )
    @NotEmpty(message = "Name can not be null or empty")
    private String name;

    @Schema(
            description = "Description of category", example = "Java is a programing language"
    )
    @NotEmpty(message = "Description can not be null or empty")
    private String description;

    @Schema(
            description = "Code of category", example = "java"
    )
    @NotEmpty(message = "Code can not be null or empty")
    @Pattern(regexp = "^([a-z0-9](-[a-z0-9]+)*)+$", message = "Code is not in valid format")
    private String code;

    @Schema(
            description = "Parent category id of category", example = "xxx"
    )
    private String parentCategoryId;

}
