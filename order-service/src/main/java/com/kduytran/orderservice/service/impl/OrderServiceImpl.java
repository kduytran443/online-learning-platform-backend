package com.kduytran.orderservice.service.impl;

import com.kduytran.orderservice.converter.OrderConverter;
import com.kduytran.orderservice.converter.OrderDetailsConverter;
import com.kduytran.orderservice.dto.OrderRequestDTO;
import com.kduytran.orderservice.dto.OrderResponseDTO;
import com.kduytran.orderservice.dto.PayingOrderDTO;
import com.kduytran.orderservice.entity.OrderDetailsEntity;
import com.kduytran.orderservice.entity.OrderEntity;
import com.kduytran.orderservice.entity.OrderStatus;
import com.kduytran.orderservice.event.AbstractOrderEvent;
import com.kduytran.orderservice.event.EventType;
import com.kduytran.orderservice.event.OrderCreatedEvent;
import com.kduytran.orderservice.event.OrderDetails;
import com.kduytran.orderservice.event.pricing.PriceDTO;
import com.kduytran.orderservice.event.processor.PricingStreamsProcessor;
import com.kduytran.orderservice.exception.ResourceNotFoundException;
import com.kduytran.orderservice.repository.OrderDetailsRepository;
import com.kduytran.orderservice.repository.OrderRepository;
import com.kduytran.orderservice.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ApplicationEventPublisher publisher;
    private final PricingStreamsProcessor pricingStreamsProcessor;

    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository, ApplicationEventPublisher publisher, PricingStreamsProcessor pricingStreamsProcessor) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.publisher = publisher;
        this.pricingStreamsProcessor = pricingStreamsProcessor;
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

        List<OrderDetailsEntity> detailsEntityList = getOrderDetailsEntity(dto, orderEntity);
        orderDetailsRepository.saveAll(detailsEntityList);

        pushEvent(orderEntity, detailsEntityList, EventType.CREATED, dto, orderEntity.getAmount());
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

    private List<OrderDetailsEntity> getOrderDetailsEntity(OrderRequestDTO dto, OrderEntity entity) {
        return dto.getOrderDetails().stream().map(detailsDTO -> {
            OrderDetailsEntity detailsEntity = OrderDetailsConverter.convert(detailsDTO, new OrderDetailsEntity());
            detailsEntity.setOrder(entity);
            detailsEntity.setPrice(getPrice(detailsDTO.getTargetId().toString()));
            return detailsEntity;
        }).collect(Collectors.toList());
    }

    private Double getPrice(String targetId) {
        PriceDTO priceDTO = pricingStreamsProcessor.findByKey(targetId);
        if (priceDTO == null) {
            return null;
        }
        return priceDTO.getAmount().doubleValue();
    }

    private void pushEvent(OrderEntity entity, List<OrderDetailsEntity> orderDetailsEntities, EventType type, OrderRequestDTO dto, Double total) {
        AbstractOrderEvent event = switch (type) {
            case CREATED -> new OrderCreatedEvent();
        };
        makeEvent(event, entity, dto, orderDetailsEntities);
        event.setTotal(total);
        publisher.publishEvent(event);
    }

    private void makeEvent(AbstractOrderEvent event,
                           OrderEntity entity, OrderRequestDTO dto, List<OrderDetailsEntity> orderDetailsEntities) {
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
        event.setOrderDetailsList(orderDetailsEntities.stream().map(detailsEntity -> {
            OrderDetails details = new OrderDetails();
            details.setName(detailsEntity.getName());
            details.setPrice(detailsEntity.getPrice());
            details.setTargetId(detailsEntity.getTargetId());
            return details;
        }).collect(Collectors.toList()));
    }

}
