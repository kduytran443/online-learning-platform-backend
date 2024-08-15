package com.kduytran.paymentservice.controller;

import com.kduytran.paymentservice.constant.ResponseConstant;
import com.kduytran.paymentservice.dto.*;
import com.kduytran.paymentservice.service.IPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);
    private final IPaymentService paymentService;

    @Operation(
            summary = "Create PayPal Payment",
            description = "Initiates a PayPal payment transaction."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully created PayPal payment",
                    content = @Content(
                            schema = @Schema(implementation = PaypalResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @PostMapping("/paypal/make")
    public ResponseEntity<PaypalResponseDTO> createPaypalPayment(@RequestBody PaymentRequestDTO dto) {
        LOGGER.info("Creating PayPal payment with request: {}", dto);
        return ResponseEntity.ok(paymentService.createPaypalTransaction(dto));
    }

    @Operation(
            summary = "Cancel Pending PayPal Payment",
            description = "Cancel a pending payment transaction."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully cancelled PayPal payment",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            )
    })
    @PostMapping("/cancel")
    public ResponseEntity<ResponseDTO> cancelPayment(@RequestBody ExecutePaypalTransactionRequestDTO dto) {
        LOGGER.info("Cancelling Payment with request: {}", dto);
        paymentService.cancelPaypalTransaction(dto.getPaymentId(), dto.getPayerId());
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @Operation(
            summary = "Execute PayPal Payment",
            description = "Executes a PayPal payment transaction after the user has approved it."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully executed PayPal payment"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            )
    })
    @PostMapping("/paypal/execute")
    public ResponseEntity<ResponseDTO> executePayment(@RequestBody ExecutePaypalTransactionRequestDTO dto) {
        LOGGER.info("Executing PayPal payment with request: paymentId={}, payerId={}",
                dto.getPaymentId(), dto.getPayerId());
        boolean result = paymentService.executePaypalTransaction(dto.getPaymentId(), dto.getPayerId());
        if (result) {
            LOGGER.info("PayPal payment executed successfully with request: paymentId={}, payerId={}",
                    dto.getPaymentId(), dto.getPayerId());
            return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
        } else {
            LOGGER.error("Cannot execute PayPal with request: paymentId={}, payerId={}",
                    dto.getPaymentId(), dto.getPayerId());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(ResponseDTO.of(ResponseConstant.STATUS_417, ResponseConstant.MESSAGE_417_EXECUTION));
        }
    }

}
