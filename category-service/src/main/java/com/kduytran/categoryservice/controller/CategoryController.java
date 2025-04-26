package com.kduytran.categoryservice.controller;

import com.kduytran.categoryservice.dto.*;
import com.kduytran.categoryservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        path = "/api/v1/categories",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
@AllArgsConstructor
class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<CategoryDTO> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/{code}")
    @ResponseStatus(HttpStatus.OK)
    CategoryDTO getOneByCode(@PathVariable String code) {
        return categoryService.getOneByCode(code);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    void create(@Valid @RequestBody SaveCategoryDTO dto) {
        categoryService.create(dto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    void update(@Valid @RequestBody SaveCategoryDTO dto) {
        categoryService.update(dto);
    }

    @DeleteMapping("/{id}/rebound")
    @ResponseStatus(HttpStatus.OK)
    void rebound(@PathVariable String id) {
        categoryService.rebound(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void delete(@PathVariable("id") String id) {
        categoryService.delete(id);
    }
}
