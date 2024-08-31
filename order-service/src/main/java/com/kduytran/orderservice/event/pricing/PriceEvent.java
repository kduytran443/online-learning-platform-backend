package com.kduytran.orderservice.event.pricing;

import com.kduytran.orderservice.event.EventType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PriceEvent {
    private UUID correlationId;
    private UUID id;
    private UUID targetId;
    private BigDecimal amount;
    private EntityStatus status;
    private TargetType targetType;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
    private String closeReason;
    private EventType eventType;
}
