package com.kduytran.classqueryservice.controller;

import com.kduytran.classqueryservice.dto.ServiceContactInfoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(
        name = "CRUD REST APIs for class microservice"
)
@RequestMapping(
        path = "/api/v1/class-queries",
        produces = { MediaType.APPLICATION_JSON_VALUE }
)
@Validated
@AllArgsConstructor
public class ClassQueryController {

    private final ServiceContactInfoDTO serviceContactInfoDTO;

    @Operation(
            summary = "Get contact info",
            description = "REST API to get contract info of class microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            )
    })
    @GetMapping("/contact-info")
    public ResponseEntity<ServiceContactInfoDTO> getContactInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(serviceContactInfoDTO);
    }

}
