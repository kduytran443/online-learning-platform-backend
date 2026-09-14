package com.hudsontran.courseservice.dto.course;

import com.hudsontran.courseservice.dto.category.CategoryResponse;
import com.hudsontran.courseservice.dto.classes.ClassResponse;
import com.hudsontran.courseservice.enums.CourseLevel;
import com.hudsontran.courseservice.enums.CourseStatus;
import com.hudsontran.courseservice.enums.CourseVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {

    private UUID id;

    private String title;

    private String slug;

    private String shortDescription;

    private String description;

    private CourseLevel level;

    private String language;

    private String thumbnailUrl;

    private UUID authorId;

    private CourseStatus status;

    private CourseVisibility visibility;

    private Instant publishedAt;

    private Instant createdAt;

    private Instant updatedAt;

    private Set<UUID> categoryIds;

    private Set<UUID> classIds;

    private List<CategoryResponse> categories;

    private List<ClassResponse> classes;
}
