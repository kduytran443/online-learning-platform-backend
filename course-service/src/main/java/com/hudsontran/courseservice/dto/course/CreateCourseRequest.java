package com.hudsontran.courseservice.dto.course;

import com.hudsontran.courseservice.enums.CourseLevel;
import com.hudsontran.courseservice.enums.CourseVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record CreateCourseRequest(
        @NotBlank
        @Size(max = 255)
        String title,

        @NotBlank
        @Size(max = 255)
        String slug,

        @Size(max = 1000)
        String shortDescription,

        String description,

        CourseLevel level,

        @Size(max = 20)
        String language,

        String thumbnailUrl,

        @NotEmpty
        Set<UUID> categoryIds,

        CourseVisibility visibility
) {
}
