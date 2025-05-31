package com.kduytran.orderservice.event;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderDetails {
    private UUID targetId;
    private String name;
    private Double price;
}
