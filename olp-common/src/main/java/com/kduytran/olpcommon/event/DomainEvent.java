package com.kduytran.olpcommon.event;

import java.time.ZonedDateTime;

public abstract class DomainEvent<T> {
    private final T data;
    private final ZonedDateTime zonedDateTime;

    protected DomainEvent(T data, ZonedDateTime zonedDateTime) {
        this.data = data;
        this.zonedDateTime = zonedDateTime;
    }

    public T getData() {
        return data;
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }
}
