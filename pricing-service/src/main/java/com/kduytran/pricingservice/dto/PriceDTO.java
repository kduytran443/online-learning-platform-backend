package com.kduytran.pricingservice.dto;

import com.kduytran.pricingservice.entity.Currency;
import com.kduytran.pricingservice.entity.EntityStatus;
import com.kduytran.pricingservice.entity.TargetType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PriceDTO {
    @NotNull
    private UUID id;

    private UUID targetId;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    private Currency currency;

    private TargetType targetType;

    private LocalDateTime createdAt;

    private LocalDateTime closedAt;

    private String closeReason;

    private EntityStatus status;

}
