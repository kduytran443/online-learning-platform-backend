package com.kduytran.classresourceservice.dto;

import com.kduytran.classresourceservice.entity.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicDTO {

    @Schema(
            description = "ID of the topic", example = "e6f91b2a-3e77-4c70-a7b4-8fcad8a0b111"
    )
    private String id;

    @Schema(
            description = "Name of the topic", example = "Advanced Java"
    )
    private String name;

    @Schema(
            description = "Status of the topic", example = "ACTIVE"
    )
    private EntityStatus status;

    @Schema(
            description = "Class ID associated with the topic", example = "e6f91b2a-3e77-4c70-a7b4-8fcad8a0b111"
    )
    private String classId;

    private List<LessonDTO> lessons;

}
