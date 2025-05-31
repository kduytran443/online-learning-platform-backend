package com.kduytran.paymentservice.entity;

import lombok.Data;

import java.util.List;

@Data
public class OrderDetailsInfo {
    private List<OrderDetails> orderDetailsList;
}
