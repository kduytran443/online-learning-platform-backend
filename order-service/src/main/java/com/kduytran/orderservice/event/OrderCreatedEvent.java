package com.kduytran.orderservice.event;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderCreatedEvent extends AbstractOrderEvent {
    @Override
    public EventType getAction() {
        return EventType.CREATED;
    }
}
