package com.kduytran.paymentservice.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class PaymentCreatedEvent extends AbstractPaymentEvent {

    @Override
    public PaymentEventType getAction() {
        return PaymentEventType.CREATE;
    }

}
