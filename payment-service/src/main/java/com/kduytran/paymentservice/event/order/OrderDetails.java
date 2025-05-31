package com.kduytran.paymentservice.event.order;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderDetails {
    private UUID targetId;
    private String name;
    private double price;
}
