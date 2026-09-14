package com.hudsontran.courseservice.dto.classes;

import com.hudsontran.courseservice.enums.AccessPolicy;
import com.hudsontran.courseservice.enums.ClassStatus;
import com.hudsontran.courseservice.enums.ClassVisibility;
import com.hudsontran.courseservice.enums.EnrollmentPolicy;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record ClassResponse(
        UUID id,
        String name,
        UUID instructorId,
        String instructorName,
        BigDecimal price,
        Integer maxStudents,
        Integer currentStudents,
        ClassStatus status,
        ClassVisibility visibility,
        EnrollmentPolicy enrollmentPolicy,
        AccessPolicy accessPolicy,
        Instant createdAt,
        Instant updatedAt
) {
}
