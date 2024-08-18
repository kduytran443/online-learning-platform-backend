package com.kduytran.orderservice.entity;

import com.kduytran.orderservice.entity.attrconverter.UserInfoAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order")
@Getter @Setter
public class OrderEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private OrderStatus status;

    @Column
    private OrderType type;

    @Column
    private Double amount;

    @Column
    private String paymentId;

    @Column
    private String paymentUrl;

    @Column
    @Convert(converter = UserInfoAttributeConverter.class)
    private UserInfo userInfo;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime executeAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<OrderDetailsEntity> orderDetailsList;

}
