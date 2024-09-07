package com.kduytran.memberservice.event;

import lombok.Data;

import java.util.List;

@Data
public class OrderDetailsInfo {
    private List<OrderDetails> orderDetailsList;
}
