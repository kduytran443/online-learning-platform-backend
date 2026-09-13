package com.hudsontran.courseservice.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record CourseReportData(
        UUID id,
        String title,
        String slug,
        String shortDescription,
        String description,
        String level,
        String language,
        UUID authorId,
        String status,
        String visibility,
        Instant publishedAt,
        Instant createdAt,
        Instant updatedAt,
        List<ClassReportData> classes
) {
}
