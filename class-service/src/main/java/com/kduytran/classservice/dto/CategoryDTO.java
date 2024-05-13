package com.kduytran.classservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CategoryDTO {

    @Schema(
            description = "Id of category", example = "xxx"
    )
    private String id;

    @Schema(
            description = "Name of category", example = "Java"
    )
    private String name;

    @Schema(
            description = "Code of category", example = "java"
    )
    private String code;

    private CategoryDTO parentCategory;

}
