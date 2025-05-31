package com.kduytran.orderservice.entity;

import com.kduytran.orderservice.entity.attrconverter.AppliedCouponsInfoAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "order_details")
@Getter @Setter
public class OrderDetailsEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private UUID targetId;

    @Column
    private String name;

    @Column
    private Double price;

    @Column
    @Convert(converter = AppliedCouponsInfoAttributeConverter.class)
    private AppliedCouponsInfo couponsInfo;

    @Column
    private Double discountedPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderEntity order;

}
