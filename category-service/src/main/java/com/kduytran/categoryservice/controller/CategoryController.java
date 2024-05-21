package com.kduytran.categoryservice.controller;

import com.kduytran.categoryservice.constant.ResponseConstant;
import com.kduytran.categoryservice.dto.CategoryDTO;
import com.kduytran.categoryservice.dto.CreateCategoryDTO;
import com.kduytran.categoryservice.dto.ErrorResponseDTO;
import com.kduytran.categoryservice.dto.ResponseDTO;
import com.kduytran.categoryservice.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(
        name = "CRUD REST APIs for category microservice"
)
@RequestMapping(
        path = "/api/v1/categories",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class CategoryController {
    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
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
    public ResponseEntity<ResponseDTO> createCategory(@RequestBody CreateCategoryDTO categoryDTO) {
        categoryService.create(categoryDTO);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.MESSAGE_201));
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
