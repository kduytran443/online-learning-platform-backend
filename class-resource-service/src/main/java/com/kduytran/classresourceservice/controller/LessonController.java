package com.kduytran.classresourceservice.controller;

import com.kduytran.classresourceservice.constant.ResponseConstant;
import com.kduytran.classresourceservice.dto.*;
import com.kduytran.classresourceservice.entity.EntityStatus;
import com.kduytran.classresourceservice.service.ILessonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(
        name = "CRUD REST APIs for lesson controller"
)
@RequestMapping(
        path = "/api/v1/lessons",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
@AllArgsConstructor
public class LessonController {
    private final ILessonService lessonService;

    @PostMapping
    public ResponseEntity<IdResponseDTO> createLesson(@Valid @RequestBody CreateLessonDTO dto) {
        UUID id = lessonService.create(dto);
        return ResponseEntity.ok(IdResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.MESSAGE_201, id));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateLesson(@Valid @RequestBody UpdateLessonDTO dto) {
        lessonService.update(dto);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteLesson(@PathVariable String id) {
        lessonService.delete(id);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @PutMapping("/hide/{id}")
    public ResponseEntity<ResponseDTO> hideLesson(@PathVariable String id) {
        lessonService.hide(id);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @PutMapping("/next-seq/{id}")
    public ResponseEntity<ResponseDTO> updateNextSeq(@PathVariable String id) {
        lessonService.updateNextSeq(id);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @PutMapping("/previous-seq/{id}")
    public ResponseEntity<ResponseDTO> updatePreviousSeq(@PathVariable String id) {
        lessonService.updatePreviousSeq(id);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<List<LessonDTO>> findAllByTopicId(@PathVariable String topicId,
                                                           @RequestParam List<EntityStatus> statuses) {
        List<LessonDTO> lessonDTOs = lessonService.findAllByTopicId(topicId, statuses);
        return ResponseEntity.ok(lessonDTOs);
    }

}
