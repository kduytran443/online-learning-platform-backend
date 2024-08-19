package com.kduytran.orderservice.service.impl;

import com.kduytran.orderservice.converter.OrderConverter;
import com.kduytran.orderservice.dto.OrderRequestDTO;
import com.kduytran.orderservice.dto.OrderResponseDTO;
import com.kduytran.orderservice.dto.PayingOrderDTO;
import com.kduytran.orderservice.entity.OrderEntity;
import com.kduytran.orderservice.entity.OrderStatus;
import com.kduytran.orderservice.exception.ResourceNotFoundException;
import com.kduytran.orderservice.repository.OrderRepository;
import com.kduytran.orderservice.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public UUID placeAnOrder(OrderRequestDTO dto) {
        // TODO: Validate the order by calling APIs
        validate(dto);

        OrderEntity orderEntity = OrderConverter.convert(dto, new OrderEntity());
        orderEntity.setCreatedAt(LocalDateTime.now());
        orderEntity.setStatus(OrderStatus.CREATED);

        orderEntity = orderRepository.save(orderEntity);

        return orderEntity.getId();
    }

    @Override
    public void makeOrderFailed(String id) {
        OrderEntity orderEntity = orderRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("order", "id", id)
        );
        orderEntity.setStatus(OrderStatus.FAILED);
        orderRepository.save(orderEntity);
    }

    @Override
    public void makeOrderPaying(PayingOrderDTO dto) {
        log.info("Correlation ID: {} - Initiating payment process for order ID: {}",
                dto.getCorrelationId(), dto.getOrderId());

        OrderEntity orderEntity = orderRepository.findById(UUID.fromString(dto.getOrderId())).orElseThrow(
                () -> new ResourceNotFoundException("order", "id", dto.getOrderId())
        );
        orderEntity.setStatus(OrderStatus.PAYING);
        orderEntity.setExecutedAt(LocalDateTime.now());
        orderEntity.setPaymentId(dto.getPaymentId());
        orderEntity.setPaymentUrl(dto.getPaymentUrl());

        orderRepository.save(orderEntity);
    }

    @Override
    public void cancelOrder(String id) {
        OrderEntity orderEntity = orderRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("order", "id", id)
        );
        orderEntity.setStatus(OrderStatus.FAILED);
        orderRepository.save(orderEntity);
    }

    @Override
    public void makeOrderPaid(String id) {
        OrderEntity orderEntity = orderRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("order", "id", id)
        );
        orderEntity.setStatus(OrderStatus.PAID);
        orderRepository.save(orderEntity);
    }

    @Override
    public OrderResponseDTO getOrder(String id) {
        OrderEntity orderEntity = orderRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("order", "id", id)
        );
        return OrderConverter.convert(orderEntity);
    }


    public void validate(OrderRequestDTO dto) {

    }

}
