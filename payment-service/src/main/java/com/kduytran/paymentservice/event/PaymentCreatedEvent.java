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

    @Override
    public PaymentEventType getAction() {
        return PaymentEventType.CREATE;
    }

}
