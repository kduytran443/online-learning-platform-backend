package com.kduytran.classresourceservice.converter;

import com.kduytran.classresourceservice.dto.CreateTopicDTO;
import com.kduytran.classresourceservice.dto.TopicDTO;
import com.kduytran.classresourceservice.dto.UpdateTopicDTO;
import com.kduytran.classresourceservice.entity.EntityStatus;
import com.kduytran.classresourceservice.entity.TopicEntity;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class TopicConverter {

    public static TopicEntity convert(TopicDTO dto, TopicEntity entity) {
        if (entity == null) {
            entity = new TopicEntity();
        }
        entity.setName(dto.getName());
        entity.setStatus(EntityStatus.of(dto.getStatus()));
        entity.setClassId(UUID.fromString(dto.getClassId()));
        // set seq
        return entity;
    }

    public static TopicEntity convert(CreateTopicDTO dto, TopicEntity entity) {
        if (entity == null) {
            entity = new TopicEntity();
        }
        entity.setName(dto.getName());
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        } else {
            entity.setStatus(EntityStatus.HIDDEN);
        }
        entity.setClassId(UUID.fromString(dto.getClassId()));
        return entity;
    }

    public static TopicEntity convert(UpdateTopicDTO dto, TopicEntity entity) {
        if (entity == null) {
            entity = new TopicEntity();
        }
        entity.setName(dto.getName());
        if (dto.getStatus() != null) {
            entity.setStatus(EntityStatus.of(dto.getStatus()));
        } else {
            entity.setStatus(EntityStatus.HIDDEN);
        }
        return entity;
    }

    public static TopicDTO convert(TopicEntity entity, TopicDTO dto) {
        if (dto == null) {
            dto = new TopicDTO();
        }
        dto.setId(entity.getId().toString());
        dto.setClassId(entity.getClassId().toString());
        dto.setStatus(entity.getStatus().getCode());
        dto.setName(entity.getName());
        // set topic items
        return dto;
    }

}
