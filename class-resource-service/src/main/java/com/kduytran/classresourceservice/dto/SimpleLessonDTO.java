package com.kduytran.classresourceservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SimpleLessonDTO {
    private UUID id;
    private Integer seq;
    private String name;
}
