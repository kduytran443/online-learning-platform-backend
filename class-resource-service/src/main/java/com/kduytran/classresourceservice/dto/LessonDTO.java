package com.kduytran.classresourceservice.dto;

import lombok.Data;

@Data
public class LessonDTO {
    private String id;
    private Integer seq;
    private String name;
    private String description;
    private SimpleTopicDTO topic;
}
