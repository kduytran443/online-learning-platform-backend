package com.kduytran.classservice.controller;

import com.kduytran.classservice.constant.ResponseConstant;
import com.kduytran.classservice.dto.*;
import com.kduytran.classservice.service.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.UUID;

@Tag(name = "CRUD REST APIs for class entities")
@RestController
@RequiredArgsConstructor
public class ClassController {

    public static final String URI_PATH = "/api/v1/classes";
    private final ClassService classService;

    @Operation(
            summary = "Create new class REST API",
            description = "REST API to create new class inside the system"
    )
    @PostMapping(value = URI_PATH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UUID>> createClass(
            @RequestPart("saveClassDTO") @Valid SaveClassDTO saveClassDTO,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestPart(value = "banner", required = false) MultipartFile banner) {
        UUID id = classService.create(saveClassDTO, thumbnail, banner);
        return ResponseEntity
                .created(URI.create(URI_PATH + "/" + id))
                .body(ApiResponse.<UUID>builder()
                        .status(ResponseConstant.STATUS_201)
                        .message(ResponseConstant.MESSAGE_201)
                        .data(id)
                        .build()
                );
    }

    @Operation(
            summary = "Update existed class REST API",
            description = "REST API to update existed class inside the system"
    )
    @PutMapping(URI_PATH + "/{id}")
    public ResponseEntity<ApiResponse<Void>> updateClass(@PathVariable("id") UUID id,
                                                   @Valid @RequestBody SaveClassDTO saveClassDTO) {
        classService.update(id, saveClassDTO);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                        .status(ResponseConstant.STATUS_200)
                        .message(ResponseConstant.MESSAGE_200).build()
        );
    }

    @Operation(
            summary = "Delete existed class REST API",
            description = "REST API to delete existed class inside the system"
    )
    @DeleteMapping(URI_PATH + "/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable("id") UUID id) {
        classService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
