package com.kduytran.classservice.controller;

import com.kduytran.classservice.constant.ResponseConstant;
import com.kduytran.classservice.dto.*;
import com.kduytran.classservice.service.IClassService;
import com.kduytran.classservice.service.client.CategoryFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
        path = "/api/v1/classes",
        produces = { MediaType.APPLICATION_JSON_VALUE }
)
@Validated
public class ClassController {

    private final IClassService classService;
    private final CategoryFeignClient categoryFeignClient;

    public ClassController(IClassService classService, CategoryFeignClient categoryFeignClient) {
        this.classService = classService;
        this.categoryFeignClient = categoryFeignClient;
    }

    @Operation(
            summary = "Create new class REST API",
            description = "REST API to create new class inside the system"
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
    public ResponseEntity<IdResponseDTO> createClass(@Valid @RequestBody UpdateClassDTO updateClassDTO) {
        UUID id = classService.create(updateClassDTO);
        return ResponseEntity.ok(IdResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.MESSAGE_201, id));
    }

    @Operation(
            summary = "Update existed class REST API",
            description = "REST API to update existed class inside the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_200,
                    description = "HTTP Status UPDATED"
            ),
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_500,
                    description = "HTTP Status INTERNAL SERVER ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateClass(@PathVariable("id") String id, @Valid @RequestBody UpdateClassDTO updateClassDTO) {
        classService.update(id, updateClassDTO);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @Operation(
            summary = "Delete existed class REST API",
            description = "REST API to delete existed class inside the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_200,
                    description = "HTTP Status DELETED"
            ),
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_500,
                    description = "HTTP Status INTERNAL SERVER ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteClass(@PathVariable("id") String id) {
        classService.delete(id);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @Operation(
            summary = "Hide existed class REST API",
            description = "REST API to hide existed class inside the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_200,
                    description = "HTTP Status HIDE"
            ),
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_500,
                    description = "HTTP Status INTERNAL SERVER ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @PutMapping("/{id}/hide")
    public ResponseEntity<ResponseDTO> hideClass(@PathVariable("id") String id) {
        classService.hide(id);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @Operation(
            summary = "Un-Hide existed class REST API",
            description = "REST API to un hide hidden class inside the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_200,
                    description = "HTTP Status UN-HIDE"
            ),
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_500,
                    description = "HTTP Status INTERNAL SERVER ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @PutMapping("/{id}/un-hide")
    public ResponseEntity<ResponseDTO> unHideClass(@PathVariable("id") String id) {
        classService.unHide(id);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @Operation(
            summary = "Rebound the deleted class REST API",
            description = "REST API to rebound the deleted class inside the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_200,
                    description = "HTTP Status REBOUND"
            ),
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_500,
                    description = "HTTP Status INTERNAL SERVER ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @PutMapping("/{id}/rebound")
    public ResponseEntity<ResponseDTO> reboundClass(@PathVariable("id") String id) {
        classService.rebound(id);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @Operation(
            summary = "Rebound the deleted class REST API",
            description = "REST API to rebound the deleted class inside the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_200,
                    description = "HTTP Status REBOUND"
            ),
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_500,
                    description = "HTTP Status INTERNAL SERVER ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @PostMapping("/{id}/check-password")
    public ResponseEntity<ResponseDTO> checkPassword(@PathVariable("id") String id,
                                                     @Valid @RequestBody CheckPasswordDTO checkPasswordDTO) {
        boolean correct = classService.checkPassword(id, checkPasswordDTO);
        if (correct) {
            return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.PASSWORD_MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseDTO.of(ResponseConstant.STATUS_400, ResponseConstant.PASSWORD_MESSAGE_400)
            );
        }
    }

    @Operation(
            summary = "Rebound the deleted class REST API",
            description = "REST API to rebound the deleted class inside the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_200,
                    description = "HTTP Status REBOUND"
            ),
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_500,
                    description = "HTTP Status INTERNAL SERVER ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @PutMapping("/{id}/password")
    public ResponseEntity<ResponseDTO> setPassword(@PathVariable("id") String id, @Valid @RequestBody SetPasswordDTO setPasswordDTO) {
        classService.setPassword(id, setPasswordDTO);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

}
