package com.hudsontran.courseservice.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record ClassReportData(
        UUID id,
        String name,
        UUID instructorId,
        String instructorName,
        BigDecimal price,
        Integer maxStudents,
        Integer currentStudents,
        String status,
        String visibility,
        String enrollmentPolicy,
        String accessPolicy,
        Instant createdAt,
        Instant updatedAt
) {
}
