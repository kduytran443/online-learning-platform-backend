package com.kduytran.classresourceservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateLessonDTO {
    @Schema(
            description = "Id of the lesson"
    )
    @NotEmpty(message = "Id cannot be null or empty")
    private String id;

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
    @NotEmpty(message = "status cannot be null or empty")
    @Pattern(regexp = "^(L|D|H)$")
    private String status;

    @Schema(
            description = "Topic id of the lesson", example = "xxx"
    )
    @NotEmpty(message = "Topic id cannot be null or empty")
    private String topicId;
}
