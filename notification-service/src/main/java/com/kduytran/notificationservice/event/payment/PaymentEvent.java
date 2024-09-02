package com.kduytran.notificationservice.event.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentEvent {
    private UUID correlationId;
    private UUID id;
    private Double total;
    private String currency;
    private UUID orderId;
    private String description;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String payerId;
    private String paymentId;
    private String paymentUrl;
    private LocalDateTime createdAt;
    private LocalDateTime executionAt;
    private UUID userId;
    private String username;
    private String fullName;
    private String email;
    private PaymentEventType action;
    private List<OrderDetails> orderDetailsList;
}
