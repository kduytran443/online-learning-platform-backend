package com.kduytran.orderservice.dto;

import com.kduytran.orderservice.entity.OrderType;
import com.kduytran.orderservice.entity.UserInfo;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    private OrderType type;
    private Double amount;
    private UserInfo userInfo;
    private List<OrderDetailsDTO> orderDetails;
}
