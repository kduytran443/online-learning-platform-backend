package com.kduytran.paymentservice.payment;

import com.kduytran.paymentservice.dto.PaymentRequestDTO;
import com.kduytran.paymentservice.dto.PaypalPaymentIntent;
import com.kduytran.paymentservice.dto.PaypalPaymentMethod;
import com.kduytran.paymentservice.entity.PaymentMethod;
import com.kduytran.paymentservice.entity.PaymentStatus;
import com.kduytran.paymentservice.entity.TransactionEntity;
import com.kduytran.paymentservice.exception.PayPalTransactionException;
import com.kduytran.paymentservice.repository.TransactionRepository;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Data
@AllArgsConstructor
public class MasterCardStrategy implements InitPaymentStrategy {
    private final Supplier<APIContext> apiContext;
    private final TransactionRepository transactionRepository;
    private PaymentRequestDTO dto;

    public Payment createPaypalPayment() throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(dto.getCurrency());
        Double total = BigDecimal.valueOf(dto.getTotal()).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription("Description");
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(PaypalPaymentMethod.PAYPAL.toString().toLowerCase());

        Payment payment = new Payment();
        payment.setId(UUID.randomUUID().toString());
        payment.setIntent(PaypalPaymentIntent.ORDER.toString().toLowerCase());
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(dto.getCancelUrl());
        redirectUrls.setReturnUrl(dto.getSuccessUrl());
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext.get());
    }

    @Override
    public TransactionEntity init() {
        try {
            Payment payment = this.createPaypalPayment();
            String redirectUrl = payment.getLinks().stream().filter(link -> "approval_url".equals(link.getRel()))
                    .findFirst().map(Links::getHref).orElseThrow(
                            () -> new RuntimeException("Link not found!")
                    );

            TransactionEntity entity = new TransactionEntity();
            entity.setTotal(dto.getTotal());
            entity.setCurrency(dto.getCurrency());
            entity.setDescription(getDescription());
            entity.setCreatedAt(LocalDateTime.now());
            entity.setOrderId(dto.getOrderId());
            entity.setStatus(PaymentStatus.PENDING);
            entity.setPaymentMethod(PaymentMethod.PAYPAL);
            entity.setPaymentId(payment.getId());
            entity.setUsername(dto.getUsername());
            entity.setUserId(dto.getUserId());
            entity.setEmail(dto.getEmail());
            entity.setFullName(dto.getFullName());
            entity.setRedirectUrl(redirectUrl);
            return entity;
        } catch (PayPalRESTException e) {
            throw new PayPalTransactionException(
                    String.format("Error executing PayPal payment for orderId %s", dto.getOrderId()));
        }
    }

    private String getDescription() {
        return "";
    }

}
