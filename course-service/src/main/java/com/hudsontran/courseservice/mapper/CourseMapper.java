package com.hudsontran.courseservice.mapper;

import com.hudsontran.courseservice.dto.course.CourseResponse;
import com.hudsontran.courseservice.dto.course.CreateCourseRequest;
import com.hudsontran.courseservice.entity.CourseCategoryEntity;
import com.hudsontran.courseservice.entity.CourseEntity;
import com.hudsontran.courseservice.enums.CourseStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "courseCategories", ignore = true)
    @Mapping(
            target = "visibility",
            defaultExpression = "java(com.hudsontran.courseservice.enums.CourseVisibility.PRIVATE)"
    )
    CourseEntity toEntity(CreateCourseRequest request);

    @Mapping(
            target = "categoryIds",
            source = "courseCategories"
    )
    @Mapping(
            target = "classIds",
            ignore = true
    )
    @Mapping(
            target = "categories",
            ignore = true
    )
    @Mapping(
            target = "classes",
            ignore = true
    )
    CourseResponse toResponse(CourseEntity course);

    List<CourseResponse> toResponse(List<CourseEntity> courseList);

    default CourseEntity toEntity(CreateCourseRequest request, String slug, UUID authorId, CourseStatus status) {
        CourseEntity course = toEntity(request);
        course.setSlug(slug);
        course.setAuthorId(authorId);
        return course;
    }

    default Set<UUID> mapCategoryIds(
            Set<CourseCategoryEntity> courseCategories
    ) {
        if (courseCategories == null) {
            return Set.of();
        }

        return courseCategories.stream()
                .map(relation -> relation.getCategory().getId())
                .collect(Collectors.toSet());
    }
}
