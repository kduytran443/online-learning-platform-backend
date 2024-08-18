package com.kduytran.orderservice.entity;

import lombok.Data;

@Data
public class AppliedCouponsInfo {
    private String code;
    private String name;
    private Double percent;
}
