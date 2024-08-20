package com.kduytran.orderservice.dto;

import com.kduytran.orderservice.entity.OrderType;
import com.kduytran.orderservice.entity.UserInfo;
import com.kduytran.orderservice.event.payment.PaymentMethod;
import com.kduytran.orderservice.event.payment.PaymentStatus;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderRequestDTO {
    private OrderType type;
    private Double amount;
    private UserInfo userInfo;
    private List<OrderDetailsDTO> orderDetails;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private UUID userId;
    private String username;
    private String fullName;
    private String email;
    private String currency;
}
