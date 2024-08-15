package com.kduytran.paymentservice.event;

import com.kduytran.paymentservice.entity.PaymentMethod;
import com.kduytran.paymentservice.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class AbstractPaymentEvent {
    private UUID transactionId;
    private UUID id;
    private Double total;
    private String currency;
    private UUID orderId;
    private String description;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String payerId;
    private String paymentId;
    private LocalDateTime createdAt;
    private LocalDateTime executionAt;
    private UUID userId;
    private String username;
    private String fullName;
    private String email;

    public abstract PaymentEventType getAction();
}
