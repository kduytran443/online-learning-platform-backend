package com.kduytran.classqueryservice.dto;

import lombok.Data;

@Data
public class UpdateCategoryDTO {
    private String id;
    private String name;
    private String description;
    private String code;
    private String status;
    private String parentCategoryId;
}
