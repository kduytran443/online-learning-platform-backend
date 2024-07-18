package com.kduytran.classqueryservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @Schema(
            description = "Parent category id of category", example = "coding"
    )
    private String parentCategoryId;

}
