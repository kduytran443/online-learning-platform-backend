package com.kduytran.categoryservice.controller;

import com.kduytran.categoryservice.constant.ResponseConstant;
import com.kduytran.categoryservice.dto.*;
import com.kduytran.categoryservice.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
        name = "CRUD REST APIs for category microservice"
)
@RequestMapping(
        path = "/api/v1/categories",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
@AllArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;
    private final ServiceContactInfoDTO serviceContactInfoDTO;

    @Operation(
            summary = "Get contact info",
            description = "REST API to get contract info of category microservice"
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


    @Operation(
            summary = "Get category REST API",
            description = "REST API to category inside the bank"
    )
    @ApiResponse(
            responseCode = ResponseConstant.STATUS_200,
            description = "HTTP Status OK"
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable("id") String id) {
        return ResponseEntity.ok(categoryService.getOneById(id));
    }

    @Operation(
            summary = "Get category REST API",
            description = "REST API to category inside the bank"
    )
    @ApiResponse(
            responseCode = ResponseConstant.STATUS_200,
            description = "HTTP Status OK"
    )
    @GetMapping("/code/{code}")
    public ResponseEntity<CategoryDTO> getCategoryByCode(@PathVariable("code") String code) {
        return ResponseEntity.ok(categoryService.getOneByCode(code));
    }

    @Operation(
            summary = "Get category REST API",
            description = "REST API to category inside the bank"
    )
    @ApiResponse(
            responseCode = ResponseConstant.STATUS_200,
            description = "HTTP Status OK"
    )
    @GetMapping("/root-list")
    public ResponseEntity<List<CategoryDTO>> getRootCategories() {
        return ResponseEntity.ok(categoryService.getRootCategoryList());
    }

    @Operation(
            summary = "Create new Category REST API",
            description = "REST API to create new Category inside the system"
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
    public ResponseEntity<IdResponseDTO> createCategory(@RequestBody CreateCategoryDTO categoryDTO) {
        UUID id = categoryService.create(categoryDTO);
        return ResponseEntity.ok(IdResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.MESSAGE_201, id));
    }

    @Operation(
            summary = "Create new Category REST API",
            description = "REST API to create new Category inside the system"
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
    @PutMapping("/{categoryId}")
    public ResponseEntity<ResponseDTO> updateCategory(@RequestBody CreateCategoryDTO categoryDTO, @PathVariable(
            "categoryId") String categoryId) {
        categoryService.update(categoryId, categoryDTO);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.MESSAGE_201));
    }

    @Operation(
            summary = "Hide category temporarily REST API",
            description = "REST API to hide category and all its child inside the system temporarily"
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
    @PutMapping("/{categoryId}/hide")
    public ResponseEntity<ResponseDTO> hideCategory(@PathVariable("categoryId") String categoryId) {
        categoryService.hide(categoryId);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @Operation(
            summary = "Un hide category REST API",
            description = "REST API to unhide category and all its child inside the system"
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
    @PutMapping("/{categoryId}/un-hide")
    public ResponseEntity<ResponseDTO> unHideCategory(@PathVariable("categoryId") String categoryId) {
        categoryService.unhide(categoryId);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @Operation(
            summary = "Rebound category REST API",
            description = "REST API to rebound category and all its child inside the system"
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
    @PutMapping("/{categoryId}/rebound")
    public ResponseEntity<ResponseDTO> reboundCategory(@PathVariable("categoryId") String categoryId) {
        categoryService.rebound(categoryId);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @Operation(
            summary = "Delete category REST API",
            description = "REST API to delete category and all its child inside the system"
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
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ResponseDTO> deleteCategory(@PathVariable("categoryId") String categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

}
