package com.kduytran.orderservice.converter;

import com.kduytran.orderservice.dto.OrderRequestDTO;
import com.kduytran.orderservice.entity.OrderEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderConverter {

    public static OrderEntity convert(OrderRequestDTO dto, OrderEntity entity) {
        if (entity == null) {
            entity = new OrderEntity();
        }
        entity.setAmount(dto.getAmount());
        entity.setType(dto.getType());
        entity.setUserInfo(dto.getUserInfo());
        return entity;
    }

}
