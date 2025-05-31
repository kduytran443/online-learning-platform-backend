package com.kduytran.notificationservice.event.payment;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderDetails {
    private UUID targetId;
    private String name;
    private Double price;
}
