package com.kduytran.orderservice.event.handler;

import com.kduytran.orderservice.constant.KafkaConstant;
import com.kduytran.orderservice.dto.PayingOrderDTO;
import com.kduytran.orderservice.event.payment.PaymentEvent;
import com.kduytran.orderservice.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventHandler {
    private final ModelMapper modelMapper;
    private final IOrderService orderService;

    @KafkaListener(
            topics = KafkaConstant.TOPIC_PAYMENTS,
            groupId = "order-consumers",
            containerFactory = "kafkaListener"
    )
    public void handlePaymentEvent(PaymentEvent event) {
        log.info("Correlation ID: {} - Receive event for order ID: {}",
                event.getCorrelationId(), event.getOrderId());

        switch (event.getAction()) {
            case CREATE -> {
                PayingOrderDTO dto = new PayingOrderDTO();
                dto.setPaymentId(event.getPaymentId());
                dto.setOrderId(event.getOrderId().toString());
                dto.setPaymentUrl(event.getPaymentUrl());
                dto.setCorrelationId(event.getCorrelationId().toString());
                orderService.makeOrderPaying(dto);
                break;
            }
            case EXECUTE -> orderService.makeOrderPaid(event.getOrderId().toString());
        }
    }

}
