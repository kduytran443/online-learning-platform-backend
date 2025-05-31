package com.kduytran.authmanagementservice.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseEvent {

    protected final Object source;
    private ApplicationEventPublisher eventPublisher;

    BaseEvent(Object source) {
        this.source = source;
    }

    public abstract Object getSource();

    public void trigger() {
        eventPublisher.publishEvent(this);
    }
}
