package com.kduytran.paymentservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transaction")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "total")
    private Double total;

    @Column(name = "currency")
    private String currency;

    @Column
    private UUID orderId;

    @Column
    private String description;

    @Column
    private PaymentMethod paymentMethod;

    @Column
    private PaymentStatus status;

    @Column
    private String payerId;

    @Column
    private String paymentId;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime executionAt;

    @Column
    private UUID userId;

    @Column
    private String username;

    @Column
    private String fullName;

    @Column
    private String email;

}
