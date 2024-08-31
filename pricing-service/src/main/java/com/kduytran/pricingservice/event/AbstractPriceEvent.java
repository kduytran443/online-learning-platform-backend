package com.kduytran.pricingservice.event;

import com.kduytran.pricingservice.entity.EntityStatus;
import com.kduytran.pricingservice.entity.TargetType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public abstract class AbstractPriceEvent {
    private UUID id;
    private UUID targetId;
    private BigDecimal amount;
    private EntityStatus status;
    private TargetType targetType;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
    private String closeReason;

    public abstract EventType getEventType();
}
