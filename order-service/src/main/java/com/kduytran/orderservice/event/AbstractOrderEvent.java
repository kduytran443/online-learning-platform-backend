package com.kduytran.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class AbstractOrderEvent {
    private UUID correlationId;
    private UUID orderId;

    public abstract EventType getAction();
}
