package com.kduytran.classqueryservice.event;

import lombok.Data;

@Data
public class CategoryEvent {
    private String transactionId;
    private String action;
    private String id;
    private String name;
    private String description;
    private String code;
    private String parentCategoryId;
}
