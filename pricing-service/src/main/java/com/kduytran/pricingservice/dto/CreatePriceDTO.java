package com.kduytran.pricingservice.dto;

import com.kduytran.pricingservice.entity.TargetType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreatePriceDTO {
    @NotNull
    private UUID targetId;

    @NotNull
    private TargetType targetType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    private String closeReason;
}
