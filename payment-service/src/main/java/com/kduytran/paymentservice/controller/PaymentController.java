package com.kduytran.paymentservice.controller;

import com.kduytran.paymentservice.constant.ResponseConstant;
import com.kduytran.paymentservice.dto.PaymentRequestDTO;
import com.kduytran.paymentservice.dto.PaypalPaymentIntent;
import com.kduytran.paymentservice.dto.PaypalPaymentMethod;
import com.kduytran.paymentservice.dto.ResponseDTO;
import com.kduytran.paymentservice.service.IPaymentService;
import com.kduytran.paymentservice.util.ResponseUtil;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(
        name = "CRUD REST APIs for lesson controller"
)
@RequestMapping(
        path = "/api/v1/payments",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class PaymentController {
    private static Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final IPaymentService paymentService;

    @GetMapping("/make")
    public ResponseEntity<Void> createPayment(@RequestParam String currency,
                                            @RequestParam Double total, @RequestParam PaypalPaymentMethod method) {
        final String SUCCESS_URL = "http://localhost:8140/api/v1/payments/execute";
        final String CANCEL_URL = "http://localhost:8140/api/v1/payments/fail";
        try {
            Payment payment = paymentService.createPayment(
                    new PaymentRequestDTO(total, currency, method,
                            PaypalPaymentIntent.SALE, CANCEL_URL, SUCCESS_URL)
            );
            logger.info("Payment created with state: {}", payment.getState());
            String redirectUrl = payment.getLinks().stream().filter(link -> "approval_url".equals(link.getRel()))
                    .findFirst().map(Links::getHref).orElseThrow(
                            () -> new RuntimeException("Link not found!")
                    );
            logger.info("Redirect URL for payment: {}", redirectUrl);
            return ResponseUtil.redirect(redirectUrl);
        } catch (PayPalRESTException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/execute")
    public ResponseEntity<ResponseDTO> executePayment(@RequestParam("paymentId") String paymentId,
                                                      @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paymentService.executePayment(paymentId, payerId);
            if ("approved".equals(payment.getState())) {
                return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
            } else {
                return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_417, ResponseConstant.MESSAGE_417_EXECUTION));
            }
        } catch (PayPalRESTException e) {
            throw new RuntimeException(e);
        }
    }

}
