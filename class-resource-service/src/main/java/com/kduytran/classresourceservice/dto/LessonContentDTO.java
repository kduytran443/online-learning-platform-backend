package com.kduytran.classresourceservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

@Data
public class LessonContentDTO implements Serializable {
    @NotEmpty
    private String id;

    private String content;
}
