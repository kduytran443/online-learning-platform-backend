package com.kduytran.memberservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ClassMemberSizeDTO {
    private Long id;
    private UUID classId;
    private Integer maxSize;
    private Integer currentSize;
}
