package com.kduytran.classresourceservice.controller;

import com.kduytran.classresourceservice.constant.ResponseConstant;
import com.kduytran.classresourceservice.dto.*;
import com.kduytran.classresourceservice.entity.EntityStatus;
import com.kduytran.classresourceservice.service.ITopicService;
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
        name = "CRUD REST APIs for topic controller"
)
@RequestMapping(
        path = "/api/v1/topics",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
@AllArgsConstructor
public class TopicController {
    private final ITopicService topicService;

    @PostMapping
    public ResponseEntity<IdResponseDTO> createTopic(@Valid @RequestBody CreateTopicDTO topicDTO) {
        UUID id = topicService.create(topicDTO);
        return ResponseEntity.ok(IdResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.MESSAGE_201, id));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateTopic(@Valid @RequestBody UpdateTopicDTO topicDTO) {
        topicService.update(topicDTO);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteTopic(@PathVariable String id) {
        topicService.delete(id);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @PutMapping("/hide/{id}")
    public ResponseEntity<ResponseDTO> hideTopic(@PathVariable String id) {
        topicService.hide(id);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @PutMapping("/next-seq/{id}")
    public ResponseEntity<ResponseDTO> updateNextSeq(@PathVariable String id) {
        topicService.updateNextSeq(id);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @PutMapping("/previous-seq/{id}")
    public ResponseEntity<ResponseDTO> updatePreviousSeq(@PathVariable String id) {
        topicService.updatePreviousSeq(id);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200));
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<TopicDTO>> findAllByClassId(@PathVariable String classId,
                                                           @RequestParam(required = false) List<EntityStatus> statuses) {
        List<TopicDTO> dtoList = topicService.findAllByClassId(classId,
                (statuses == null || statuses.isEmpty()) ? List.of(EntityStatus.LIVE) : statuses );
        return ResponseEntity.ok(dtoList);
    }

}
