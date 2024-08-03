package com.kduytran.classresourceservice.converter;

import com.kduytran.classresourceservice.dto.LessonContentDTO;
import com.kduytran.classresourceservice.entity.LessonContentEntity;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class LessonContentConverter {

    public static LessonContentDTO convert(LessonContentEntity entity, LessonContentDTO dto) {
        if (dto == null) {
            dto = new LessonContentDTO();
        }
        if (entity == null) {
            return dto;
        }
        dto.setContent(entity.getContent());
        dto.setId(entity.getId().toString());
        return dto;
    }

    public static LessonContentEntity convert(LessonContentDTO dto, LessonContentEntity entity) {
        if (dto == null) {
            return entity;
        }
        if (entity == null) {
            entity = new LessonContentEntity();
        }
        entity.setContent(dto.getContent());
        entity.setId(UUID.fromString(dto.getId()));
        return entity;
    }

}
