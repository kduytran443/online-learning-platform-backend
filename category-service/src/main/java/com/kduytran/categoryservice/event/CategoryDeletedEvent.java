package com.kduytran.categoryservice.event;

import lombok.Data;

@Data
public class CategoryDeletedEvent {
    private String id;
    private String name;
    private String description;
    private String code;
    private String parentCategoryId;
}
