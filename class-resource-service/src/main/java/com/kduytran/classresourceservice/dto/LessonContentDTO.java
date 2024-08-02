package com.kduytran.classresourceservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LessonContentDTO {
    @NotEmpty
    private String id;

    private String content;
}
