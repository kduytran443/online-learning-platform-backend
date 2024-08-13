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
@ToString
public class PaymentCreatedEvent extends AbstractPaymentEvent {

    public PaymentCreatedEvent(UUID transactionId, UUID id, Double total, String currency, UUID orderId,
                               String description, PaymentMethod paymentMethod, PaymentStatus status, String payerId,
                               String paymentId, LocalDateTime createdAt, LocalDateTime executionAt, UUID userId,
                               String username, String fullName, String email) {
        super(transactionId, id, total, currency, orderId, description, paymentMethod, status, payerId, paymentId,
                createdAt, executionAt, userId, username, fullName, email);
    }

    @Override
    public PaymentEventType getAction() {
        return PaymentEventType.CREATE;
    }
}
