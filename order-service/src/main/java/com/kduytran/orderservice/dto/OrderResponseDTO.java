package com.kduytran.orderservice.dto;

import com.kduytran.orderservice.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private UUID id;
    private OrderStatus status;
    private String paymentId;
    private String paymentUrl;
}
