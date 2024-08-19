package com.kduytran.orderservice.controller;

import com.kduytran.orderservice.constant.ResponseConstant;
import com.kduytran.orderservice.dto.ErrorResponseDTO;
import com.kduytran.orderservice.dto.IdResponseDTO;
import com.kduytran.orderservice.dto.OrderRequestDTO;
import com.kduytran.orderservice.dto.OrderResponseDTO;
import com.kduytran.orderservice.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(
        name = "CRUD REST APIs for class microservice"
)
@RequestMapping(
        path = "/api/v1/orders",
        produces = { MediaType.APPLICATION_JSON_VALUE }
)
@Validated
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;

    @Operation(
            summary = "Create new order REST API",
            description = "REST API to create new order inside the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_201,
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_500,
                    description = "HTTP Status INTERNAL SERVER ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<IdResponseDTO> placeOrder(@Valid @RequestBody OrderRequestDTO dto) {
        UUID id = orderService.placeAnOrder(dto);
        return ResponseEntity.ok(IdResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.MESSAGE_201, id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

}
