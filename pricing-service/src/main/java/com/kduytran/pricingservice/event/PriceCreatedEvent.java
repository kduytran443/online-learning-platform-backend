package com.kduytran.pricingservice.event;

import lombok.Data;

@Data
public class PriceCreatedEvent extends AbstractPriceEvent {
    @Override
    public EventType getEventType() {
        return EventType.CREATED;
    }
}
