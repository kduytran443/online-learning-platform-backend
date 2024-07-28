package com.kduytran.classresourceservice.dto;

import com.kduytran.classresourceservice.entity.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateLessonDTO {

    @Schema(
            description = "Name of the lesson", example = "Advanced Java"
    )
    @NotEmpty(message = "Lesson name cannot be null or empty")
    private String name;

    @Schema(
            description = "Description of the lesson", example = "Advanced Java"
    )
    @NotEmpty(message = "Description cannot be null or empty")
    private String description;

    @Schema(
            description = "status of the lesson", example = "L"
    )
    private EntityStatus status;

    @Schema(
            description = "Topic id of the lesson", example = "xxx"
    )
    @NotEmpty(message = "Topic id cannot be null or empty")
    private String topicId;

}
