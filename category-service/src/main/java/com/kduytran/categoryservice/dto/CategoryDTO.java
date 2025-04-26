package com.kduytran.categoryservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(
        name = "CategoryDTO",
        description = "Schema to hold Category information"
)
public class CategoryDTO {
    private String id;
    private String name;
    private String description;
    private String code;
    private CategoryDTO parentCategory;
    private List<CategoryDTO> subCategories;
}
