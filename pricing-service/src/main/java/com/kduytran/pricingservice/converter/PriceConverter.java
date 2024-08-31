package com.kduytran.pricingservice.converter;

import com.kduytran.pricingservice.dto.CreatePriceDTO;
import com.kduytran.pricingservice.dto.PriceDTO;
import com.kduytran.pricingservice.entity.EntityStatus;
import com.kduytran.pricingservice.entity.PriceEntity;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class PriceConverter {

    public static PriceEntity convert(CreatePriceDTO dto, PriceEntity entity) {
        if (entity == null) {
            entity = new PriceEntity();
        }
        entity.setTargetId(dto.getTargetId());
        entity.setAmount(dto.getAmount());
        entity.setTargetType(dto.getTargetType());
        entity.setStatus(EntityStatus.ACTIVE);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCurrency(dto.getCurrency());
        return entity;
    }

    public static PriceDTO convert(PriceEntity entity, PriceDTO dto) {
        if (dto == null) {
            dto = new PriceDTO();
        }
        dto.setTargetId(entity.getTargetId());
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setAmount(entity.getAmount());
        dto.setTargetType(entity.getTargetType());
        dto.setClosedAt(entity.getClosedAt());
        dto.setCloseReason(entity.getCloseReason());
        dto.setStatus(entity.getStatus());
        dto.setCurrency(entity.getCurrency());
        return dto;
    }

}
