package com.kduytran.orderservice.service;

import com.kduytran.orderservice.dto.OrderRequestDTO;
import com.kduytran.orderservice.dto.PayingOrderDTO;

import java.util.UUID;

public interface IOrderService {

    UUID placeAnOrder(OrderRequestDTO dto);
    void makeOrderFailed(String id);
    void makeOrderPaying(PayingOrderDTO dto);
    void cancelOrder(String id);
    void makeOrderPaid(String id);

}
