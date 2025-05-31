package com.kduytran.orderservice.dto;

import com.kduytran.orderservice.entity.AppliedCouponsInfo;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderDetailsDTO {
    private UUID targetId;
    private String name;
    private Double price;
    private AppliedCouponsInfo appliedCouponsInfo;
}
