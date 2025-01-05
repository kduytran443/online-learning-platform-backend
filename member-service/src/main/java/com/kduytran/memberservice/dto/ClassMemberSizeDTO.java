package com.kduytran.memberservice.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ClassMemberSizeDTO {

    private Long id;

    @NotNull
    private UUID classId;

    @NotNull
    @DecimalMin(value = "1")
    private Integer maxSize;

    @DecimalMin(value = "0")
    private Integer currentSize;
}
