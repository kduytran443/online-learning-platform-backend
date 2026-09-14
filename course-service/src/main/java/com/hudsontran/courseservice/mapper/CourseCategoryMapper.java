package com.hudsontran.courseservice.mapper;

import com.hudsontran.courseservice.entity.CategoryEntity;
import com.hudsontran.courseservice.entity.CourseCategoryEntity;
import com.hudsontran.courseservice.entity.CourseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseCategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", source = "course")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "primary", constant = "false")
    CourseCategoryEntity toEntity(
            CourseEntity course,
            CategoryEntity category
    );
}
