package com.kduytran.classresourceservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateTopicSeqDTO {
    @Schema(
            description = "ID of the topic", example = "e6f91b2a-3e77-4c70-a7b4-8fcad8a0b111"
    )
    @NotEmpty(message = "ID cannot be null or empty")
    private String id;

    @Schema(
            description = "Class ID associated with the topic", example = "e6f91b2a-3e77-4c70-a7b4-8fcad8a0b111"
    )
    @NotEmpty(message = "Class id of topic cannot be null or empty")
    private String classId;
}
