package com.kduytran.classresourceservice.dto;

import com.kduytran.classresourceservice.entity.AssignmentType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TopicItemDTO {
    private UUID id;
    private Integer seq;
    private String name;
}
