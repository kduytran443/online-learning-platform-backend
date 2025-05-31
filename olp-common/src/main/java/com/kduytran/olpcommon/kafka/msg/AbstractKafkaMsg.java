package com.kduytran.olpcommon.kafka.msg;

import java.time.Instant;

public abstract class AbstractKafkaMsg<T> {
    private final String eventId;
    private final Instant timestamp = Instant.now();
    private final String eventType;
    private final T data;

    public AbstractKafkaMsg(String eventId, String eventType, T data) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.data = data;
    }

    public AbstractKafkaMsg(String eventId, T data) {
        this.eventId = eventId;
        this.eventType = null;
        this.data = data;
    }

    public String getEventId() {
        return eventId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getEventType() {
        return eventType;
    }

    public T getData() {
        return data;
    }
}
