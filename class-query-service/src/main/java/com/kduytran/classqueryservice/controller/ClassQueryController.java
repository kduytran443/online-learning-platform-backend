package com.kduytran.classqueryservice.controller;

import com.kduytran.classqueryservice.constant.ResponseConstant;
import com.kduytran.classqueryservice.dto.*;
import com.kduytran.classqueryservice.service.IClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    private final IClassService classService;

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

    @GetMapping("/all")
    public ResponseEntity<List<ClassDTO>> getAll() {
        return ResponseEntity.ok(classService.getAllLiveStatus());
    }

    @PostMapping
    public ResponseEntity<IdResponseDTO> create(@Valid @RequestBody ClassDTO classDTO) {
        UUID id = classService.create(classDTO);
        return ResponseEntity.ok(IdResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.MESSAGE_201, id));
    }

    @PostMapping("/bulk")
    public ResponseEntity<ResponseDTO> createBulk(@Valid @RequestBody ClassDTO[] classDTOs) {
        classService.createBulk(classDTOs);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.MESSAGE_201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> update(@PathVariable String id, @Valid @RequestBody ClassDTO classDTO) {
        classService.update(id, classDTO);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable String id) {
        classService.delete(id);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @PostMapping("/search")
    public ResponseEntity<PaginationResponseDTO> search(@RequestBody SearchRequestDTO dto) {
        return ResponseEntity.ok(classService.search(dto));
    }

}
