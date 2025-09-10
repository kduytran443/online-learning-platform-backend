package com.kduytran.classservice.mapper;

import com.kduytran.classservice.dto.SaveClassDTO;
import com.kduytran.classservice.entity.ClassEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClassMapper {
    ClassEntity toEntity(SaveClassDTO saveClassDTO);
    void mapEntity(SaveClassDTO saveClassDTO, @MappingTarget ClassEntity classEntity);
}
