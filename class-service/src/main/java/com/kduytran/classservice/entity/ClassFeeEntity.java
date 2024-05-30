package com.kduytran.classservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "class_fee")
@Getter
@Setter
public class ClassFeeEntity extends BaseEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String description;

    @Column
    private FeeType feeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="class_id", nullable = false)
    private ClassEntity aClass;

    @Column
    private BigDecimal amount;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

}
