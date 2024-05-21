package com.kduytran.categoryservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Schema(
        name = "CategoryDTO",
        description = "Schema to hold Category information"
)
public class CategoryDTO {
    @Schema(
            description = "Id of category", example = "xxx"
    )
    private String id;

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

    private CategoryDTO parentCategory;
    private List<CategoryDTO> subCategories;
    private String status;
}
