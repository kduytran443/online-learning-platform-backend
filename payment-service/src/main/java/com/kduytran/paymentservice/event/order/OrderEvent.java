package com.kduytran.paymentservice.event.order;

import com.kduytran.paymentservice.entity.PaymentMethod;
import com.kduytran.paymentservice.entity.PaymentStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderEvent {
    private UUID correlationId;
    private UUID orderId;
    private Double total;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private UUID userId;
    private String username;
    private String name;
    private String email;
    private String currency;
    private String cancelUrl;
    private String successUrl;
    private EventType action;
}
