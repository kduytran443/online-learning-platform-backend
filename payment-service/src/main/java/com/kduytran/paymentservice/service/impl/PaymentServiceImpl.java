package com.kduytran.paymentservice.service.impl;

import com.kduytran.paymentservice.dto.*;
import com.kduytran.paymentservice.entity.PaymentMethod;
import com.kduytran.paymentservice.entity.PaymentStatus;
import com.kduytran.paymentservice.entity.TransactionEntity;
import com.kduytran.paymentservice.event.AbstractPaymentEvent;
import com.kduytran.paymentservice.event.PaymentCancelledEvent;
import com.kduytran.paymentservice.event.PaymentCreatedEvent;
import com.kduytran.paymentservice.event.PaymentExecutedEvent;
import com.kduytran.paymentservice.exception.PayPalTransactionException;
import com.kduytran.paymentservice.exception.ResourceNotFoundException;
import com.kduytran.paymentservice.payment.InitPaymentStrategy;
import com.kduytran.paymentservice.payment.PaymentStrategyFactory;
import com.kduytran.paymentservice.repository.TransactionRepository;
import com.kduytran.paymentservice.service.IPaymentService;
import com.kduytran.paymentservice.service.IPaypalService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements IPaypalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final Supplier<APIContext> apiContext;
    private final TransactionRepository transactionRepository;
    private final ApplicationEventPublisher publisher;

    public Payment executePaypalPayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext.get(), paymentExecute);
    }

    @Transactional
    @Override
    public PaymentResponseDTO createPaypalTransaction(PaymentRequestDTO dto) {
        InitPaymentStrategy strategy = PaymentStrategyFactory
                .getInitPaymentStrategy(apiContext,transactionRepository, dto);
        TransactionEntity entity = strategy.init();

        transactionRepository.save(entity);
        pushEvent(entity);

        PaymentResponseDTO responseDTO = new PaymentResponseDTO();
        responseDTO.setRedirectUrl(entity.getRedirectUrl());
        return responseDTO;
    }

    @Transactional
    @Override
    public boolean executePaypalTransaction(String paymentId, String payerId) {
        TransactionEntity entity = transactionRepository.findByPaymentId(paymentId).orElseThrow(
                () -> new ResourceNotFoundException("transaction", "paymentId", paymentId)
        );
        entity.setExecutionAt(LocalDateTime.now());
        entity.setPayerId(payerId);
        try {
            entity.setStatus(PaymentStatus.SUCCESSFUL);
            LOGGER.info("Payment executed with state: {}", entity.getStatus());
            Payment payment = this.executePaypalPayment(paymentId, payerId);
            return "approved".equals(payment.getState());
        } catch (PayPalRESTException e) {
            entity.setStatus(PaymentStatus.FAILED);
            LOGGER.info("Payment executed with state: {}", entity.getStatus());
            throw new PayPalTransactionException(String.format("Error executing PayPal payment %s", paymentId));
        } finally {
            LOGGER.info("Payment saved: {}", entity);
            transactionRepository.save(entity);
            pushEvent(entity);
        }
    }

    @Transactional
    @Override
    public void cancelPaypalTransaction(String paymentId, String payerId) {
        TransactionEntity entity = transactionRepository.findByPaymentId(paymentId).orElseThrow(
                () -> new ResourceNotFoundException("transaction", "paymentId", paymentId)
        );
        entity.setExecutionAt(LocalDateTime.now());
        entity.setPayerId(payerId);
        entity.setStatus(PaymentStatus.CANCELLED);
        LOGGER.info("Payment executed with state: {}", entity.getStatus());
        transactionRepository.save(entity);
        LOGGER.info("Payment saved: {}", entity);
        pushEvent(entity);
    }

    private void pushEvent(TransactionEntity entity) {
        AbstractPaymentEvent event = switch (entity.getStatus()) {
            case SUCCESSFUL -> new PaymentExecutedEvent();
            case PENDING -> new PaymentCreatedEvent();
            case FAILED, CANCELLED -> new PaymentCancelledEvent();
        };
        makeEvent(event, entity);
        publisher.publishEvent(event);
    }

    private void makeEvent(AbstractPaymentEvent event, TransactionEntity entity) {
        event.setTransactionId(entity.getId());
        event.setId(entity.getId());
        event.setTotal(entity.getTotal());
        event.setCurrency(entity.getCurrency());
        event.setOrderId(entity.getOrderId());
        event.setDescription(entity.getDescription());
        event.setPaymentMethod(entity.getPaymentMethod());
        event.setStatus(entity.getStatus());
        event.setPayerId(entity.getPayerId());
        event.setPaymentId(entity.getPaymentId());
        event.setCreatedAt(entity.getCreatedAt());
        event.setExecutionAt(entity.getExecutionAt());
        event.setUserId(entity.getUserId());
        event.setUsername(entity.getUsername());
        event.setFullName(entity.getFullName());
        event.setEmail(entity.getEmail());
    }

}
