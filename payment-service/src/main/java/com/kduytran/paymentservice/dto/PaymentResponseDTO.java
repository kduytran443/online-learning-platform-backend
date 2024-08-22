package com.kduytran.paymentservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PaymentResponseDTO {

    @Schema(description = "Status code in the response", example = "200")
    private String statusCode;

    @Schema(description = "Status message in the response", example = "Request processed successfully")
    private String statusMsg;
    private String redirectUrl;
}
