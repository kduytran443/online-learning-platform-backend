package com.kduytran.classqueryservice.converter;

import com.kduytran.classqueryservice.dto.ClassDTO;
import com.kduytran.classqueryservice.entity.ClassEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ClassConverter {

    public static ClassEntity convert(ClassDTO dto, ClassEntity entity) {
        if (entity == null) {
            entity = new ClassEntity();
        }
        entity.setName(dto.getName());
        entity.setImage(dto.getImage());
        entity.setEndAt(dto.getEndAt());
        entity.setCreatedAt(dto.getCreateAt());
        entity.setCategoryId(dto.getCategoryId());
        entity.setStatus(dto.getStatus());
        entity.setOwnerId(dto.getOwnerId());
        entity.setOwnerType(dto.getOwnerType());
        entity.setStartAt(dto.getStartAt());
        return entity;
    }

    public static ClassDTO convert(ClassEntity entity, ClassDTO dto) {
        dto.setAverageRating(entity.getAverageRating());
        dto.setNumberOfReviews(entity.getNumberOfReviews());
        dto.setId(entity.getId().toString());
        dto.setImage(entity.getImage());
        dto.setCategoryId(entity.getCategoryId());
        dto.setStatus(entity.getStatus());
        dto.setEndAt(entity.getEndAt());
        dto.setOwnerId(entity.getOwnerId());
        dto.setStartAt(entity.getStartAt());
        dto.setOwnerType(entity.getOwnerType());
        dto.setName(entity.getName());
        return dto;
    }

}
