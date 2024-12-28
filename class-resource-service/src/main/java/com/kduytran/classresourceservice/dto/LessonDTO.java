package com.kduytran.classresourceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonDTO implements Serializable {
    private String id;
    private Integer seq;
    private String name;
    private String description;
    private TopicDTO topic;
    private LessonContentDTO content;
}
