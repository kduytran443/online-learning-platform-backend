package com.kduytran.paymentservice.payment;

import com.kduytran.paymentservice.dto.PaymentRequestDTO;
import com.kduytran.paymentservice.entity.PaymentMethod;
import com.kduytran.paymentservice.entity.PaymentStatus;
import com.kduytran.paymentservice.entity.TransactionEntity;
import com.kduytran.paymentservice.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MasterCardStrategy implements InitPaymentStrategy {
    private final TransactionRepository transactionRepository;
    private PaymentRequestDTO dto;

    @Override
    public TransactionEntity init() {
        final String paymentId = "";
        final String redirectUrl = "";

        TransactionEntity entity = new TransactionEntity();
        entity.setTotal(dto.getTotal());
        entity.setCurrency(dto.getCurrency());
        entity.setDescription(getDescription());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setOrderId(dto.getOrderId());
        entity.setStatus(PaymentStatus.PENDING);
        entity.setPaymentMethod(PaymentMethod.PAYPAL);
        entity.setPaymentId(paymentId);
        entity.setUsername(dto.getUsername());
        entity.setUserId(dto.getUserId());
        entity.setEmail(dto.getEmail());
        entity.setFullName(dto.getFullName());
        entity.setRedirectUrl(redirectUrl);
        return entity;
    }

    private String getDescription() {
        return "";
    }

}
