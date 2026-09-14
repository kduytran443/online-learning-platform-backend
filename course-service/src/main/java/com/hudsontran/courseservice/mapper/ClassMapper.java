package com.hudsontran.courseservice.mapper;

import com.hudsontran.courseservice.dto.classes.ClassResponse;
import com.hudsontran.courseservice.entity.ClassEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClassMapper {

    ClassResponse toResponse(
            ClassEntity entity
    );
}
