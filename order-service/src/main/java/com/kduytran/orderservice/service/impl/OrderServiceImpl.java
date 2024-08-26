package com.kduytran.orderservice.service.impl;

import com.kduytran.orderservice.converter.OrderConverter;
import com.kduytran.orderservice.dto.OrderRequestDTO;
import com.kduytran.orderservice.dto.OrderResponseDTO;
import com.kduytran.orderservice.dto.PayingOrderDTO;
import com.kduytran.orderservice.entity.OrderEntity;
import com.kduytran.orderservice.entity.OrderStatus;
import com.kduytran.orderservice.event.AbstractOrderEvent;
import com.kduytran.orderservice.event.EventType;
import com.kduytran.orderservice.event.OrderCreatedEvent;
import com.kduytran.orderservice.exception.ResourceNotFoundException;
import com.kduytran.orderservice.repository.OrderRepository;
import com.kduytran.orderservice.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher publisher;

    public OrderServiceImpl(OrderRepository orderRepository, ApplicationEventPublisher publisher) {
        this.orderRepository = orderRepository;
        this.publisher = publisher;
    }

    @Transactional
    @Override
    public UUID placeAnOrder(OrderRequestDTO dto) {
        // TODO: Validate the order by calling APIs
        validate(dto);

        OrderEntity orderEntity = OrderConverter.convert(dto, new OrderEntity());
        orderEntity.setCreatedAt(LocalDateTime.now());
        orderEntity.setStatus(OrderStatus.CREATED);
        orderEntity = orderRepository.save(orderEntity);
        pushEvent(orderEntity, EventType.CREATED, dto, orderEntity.getAmount());
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

    private void pushEvent(OrderEntity entity, EventType type, OrderRequestDTO dto, Double total) {
        AbstractOrderEvent event = switch (type) {
            case CREATED -> new OrderCreatedEvent();
        };
        makeEvent(event, entity, dto);
        event.setTotal(total);
        publisher.publishEvent(event);
    }

    private void makeEvent(AbstractOrderEvent event,
                           OrderEntity entity, OrderRequestDTO dto) {
        event.setCorrelationId(UUID.randomUUID());
        event.setOrderId(entity.getId());
        event.setEmail(entity.getUserInfo().getEmail());
        event.setName(entity.getUserInfo().getName());
        event.setUserId(entity.getUserInfo().getId());
        event.setUsername(entity.getUserInfo().getUsername());
        event.setStatus(event.getStatus());
        event.setPaymentMethod(dto.getPaymentMethod());
        event.setCurrency(dto.getCurrency());
        event.setCancelUrl(dto.getCancelUrl());
        event.setSuccessUrl(dto.getSuccessUrl());
    }

}
