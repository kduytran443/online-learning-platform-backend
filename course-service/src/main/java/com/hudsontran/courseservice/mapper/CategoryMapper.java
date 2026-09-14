package com.hudsontran.courseservice.mapper;

import com.hudsontran.courseservice.dto.category.CategoryResponse;
import com.hudsontran.courseservice.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(
            target = "parentId",
            source = "parent.id"
    )
    CategoryResponse toResponse(
            CategoryEntity entity
    );
}
