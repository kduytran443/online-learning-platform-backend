package com.kduytran.classresourceservice.converter;

import com.kduytran.classresourceservice.dto.CreateLessonDTO;
import com.kduytran.classresourceservice.dto.LessonDTO;
import com.kduytran.classresourceservice.dto.UpdateLessonDTO;
import com.kduytran.classresourceservice.entity.EntityStatus;
import com.kduytran.classresourceservice.entity.LessonEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LessonConverter {

    public static LessonEntity convert(CreateLessonDTO dto, LessonEntity entity) {
        if (entity == null) {
            entity = new LessonEntity();
        }
        if (dto == null) {
            return entity;
        }
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    public static LessonEntity convert(UpdateLessonDTO dto, LessonEntity entity) {
        if (entity == null) {
            entity = new LessonEntity();
        }
        if (dto == null) {
            return entity;
        }
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());
        entity.setStatus(EntityStatus.of(dto.getStatus()));
        return entity;
    }

    public static LessonDTO convert(LessonEntity entity, LessonDTO dto) {
        if (dto == null) {
            dto = new LessonDTO();
        }
        if (entity == null) {
            return dto;
        }
        dto.setDescription(entity.getDescription());
        dto.setSeq(entity.getSeq());
        dto.setName(entity.getName());
        dto.setId(entity.getId().toString());
        return dto;
    }

}
