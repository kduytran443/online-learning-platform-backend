package com.kduytran.classresourceservice.service;

import com.kduytran.classresourceservice.dto.CreateLessonDTO;
import com.kduytran.classresourceservice.dto.LessonDTO;
import com.kduytran.classresourceservice.dto.UpdateLessonDTO;
import com.kduytran.classresourceservice.entity.EntityStatus;

import java.util.List;
import java.util.UUID;

public interface ILessonService {
    UUID create(CreateLessonDTO dto);
    void update(UpdateLessonDTO dto);
    void delete(String id);
    void updateNextSeq(String id);
    void updatePreviousSeq(String id);
    void hide(String id);
    List<LessonDTO> findAllByTopicId(String topicId, List<EntityStatus> statuses);
    LessonDTO getLessonDetailsById(String id);
}
