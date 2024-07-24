package com.kduytran.classresourceservice.converter;

import com.kduytran.classresourceservice.dto.LessonContentDTO;
import com.kduytran.classresourceservice.entity.LessonContentEntity;
import lombok.experimental.UtilityClass;

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

}
