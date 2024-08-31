package com.kduytran.pricingservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "price")
@Getter
@Setter
public class PriceEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private UUID targetId;

    @Column
    private BigDecimal amount;

    @Column
    private Currency currency;

    @Column
    private EntityStatus status;

    @Column
    private TargetType targetType;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime closedAt;

    @Column
    private String closeReason;

}
