package com.kduytran.orderservice.event;

import com.kduytran.orderservice.event.payment.PaymentMethod;
import com.kduytran.orderservice.event.payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class AbstractOrderEvent {
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

    public abstract EventType getAction();
}
