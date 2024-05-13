package com.kduytran.classservice.converter;

import com.kduytran.classservice.dto.SimpleClassDTO;
import com.kduytran.classservice.utils.TimeUtils;
import com.kduytran.classservice.dto.CreateClassDTO;
import com.kduytran.classservice.entity.ClassEntity;
import com.kduytran.classservice.entity.EntityStatus;
import com.kduytran.classservice.entity.OwnerType;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class ClassConverter {

    public static ClassEntity convert(CreateClassDTO dto, ClassEntity entity) {
        if (dto == null) {
            return null;
        }
        if (entity == null) {
            entity = new ClassEntity();
        }
        entity.setName(dto.getName());
        entity.setStatus(EntityStatus.of(dto.getStatus()));
        entity.setOwnerType(OwnerType.of(dto.getOwnerType()));
        // ClassDescription
        entity.setClassOwnerId(UUID.fromString(dto.getOwnerId()));
        entity.setCategoryId(UUID.fromString(dto.getCategoryId()));
        entity.setStartAt(TimeUtils.getFormattedDate(dto.getStartAt(), TimeUtils.DATE_FORMAT_YYYY_MM_DD));
        if (dto.getEndAt() != null) {
            entity.setEndAt(TimeUtils.getFormattedDate(dto.getEndAt(), TimeUtils.DATE_FORMAT_YYYY_MM_DD));
        }
        return entity;
    }

    public static SimpleClassDTO convert(ClassEntity entity, SimpleClassDTO dto) {
        if (entity == null) {
            return null;
        }
        if (dto == null) {
            dto = new SimpleClassDTO();
        }
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus().getCode());
        dto.setClassOwnerId(entity.getClassOwnerId().toString());
        // Set category
        dto.setEndAt(entity.getEndAt());
        dto.setStartAt(entity.getStartAt());
        dto.setId(entity.getId().toString());
        return dto;
    }

}
