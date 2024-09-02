package com.kduytran.orderservice.converter;

import com.kduytran.orderservice.dto.OrderDetailsDTO;
import com.kduytran.orderservice.entity.OrderDetailsEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderDetailsConverter {

    public static OrderDetailsEntity convert(OrderDetailsDTO dto, OrderDetailsEntity entity) {
        if (entity == null) {
            entity = new OrderDetailsEntity();
        }
        entity.setName(dto.getName());
        entity.setCouponsInfo(dto.getAppliedCouponsInfo());
        entity.setTargetId(dto.getTargetId());
        return entity;
    }

}
