package com.kduytran.categoryservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDTO {
    private String id;
    private String name;
    private String description;
    private String code;
    private String status;
    private CategoryDTO parentCategory;
    private List<CategoryDTO> subCategories;
}
