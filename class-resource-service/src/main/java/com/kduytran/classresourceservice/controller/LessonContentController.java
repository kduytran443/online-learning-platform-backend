package com.kduytran.classresourceservice.controller;

import com.kduytran.classresourceservice.constant.ResponseConstant;
import com.kduytran.classresourceservice.dto.*;
import com.kduytran.classresourceservice.service.ILessonContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(
        name = "CRUD REST APIs for lesson content controller"
)
@RequestMapping(
        path = "/api/v1/lesson-contents",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
@AllArgsConstructor
public class LessonContentController {
    private final ILessonContentService lessonContentService;

    @PostMapping
    public ResponseEntity<IdResponseDTO> edit(@Valid @RequestBody LessonContentDTO dto) {
        UUID id = lessonContentService.edit(dto);
        return ResponseEntity.ok(IdResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.MESSAGE_201, id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonContentDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(lessonContentService.getById(id));
    }

}
