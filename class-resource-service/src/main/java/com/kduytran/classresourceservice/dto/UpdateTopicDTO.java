package com.kduytran.classresourceservice.dto;

import com.kduytran.classresourceservice.entity.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateTopicDTO {
    @Schema(
            description = "ID of the topic", example = "e6f91b2a-3e77-4c70-a7b4-8fcad8a0b111"
    )
    @NotEmpty(message = "ID cannot be null or empty")
    private String id;

    @Schema(
            description = "Name of the topic", example = "Advanced Java"
    )
    @NotEmpty(message = "Topic name cannot be null or empty")
    private String name;

    @Schema(
            description = "Status of the topic", example = "ACTIVE"
    )
    private EntityStatus status;

    @Schema(
            description = "Class ID associated with the topic", example = "e6f91b2a-3e77-4c70-a7b4-8fcad8a0b111"
    )
    @NotEmpty(message = "Class id of topic cannot be null or empty")
    private String classId;

    private Integer seq;
}
