package com.kduytran.classresourceservice.service.impl;

import com.kduytran.classresourceservice.converter.LessonContentConverter;
import com.kduytran.classresourceservice.dto.LessonContentDTO;
import com.kduytran.classresourceservice.entity.LessonContentEntity;
import com.kduytran.classresourceservice.entity.LessonEntity;
import com.kduytran.classresourceservice.exception.ResourceNotFoundException;
import com.kduytran.classresourceservice.repository.LessonContentRepository;
import com.kduytran.classresourceservice.repository.LessonRepository;
import com.kduytran.classresourceservice.service.ILessonContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LessonContentServiceImpl implements ILessonContentService {
    private final LessonContentRepository lessonContentRepository;
    private final LessonRepository lessonRepository;

    @Override
    public UUID edit(LessonContentDTO dto) {
        LessonEntity lessonEntity = lessonRepository.findById(UUID.fromString(dto.getId())).orElseThrow(
                () -> new ResourceNotFoundException("lesson", "id", dto.getId())
        );
        LessonContentEntity entity = lessonContentRepository.findById(UUID.fromString(dto.getId())).map(
                (le) -> LessonContentConverter.convert(dto, le)
        ).orElseGet(
                () -> LessonContentConverter.convert(dto, new LessonContentEntity())
        );
        if (entity.getLesson() == null) {
            entity.setLesson(lessonEntity);
        }
        entity = lessonContentRepository.save(entity);
        return entity.getId();
    }

    @Override
    public LessonContentDTO getById(String id) {
        LessonContentEntity entity = lessonContentRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("lessonContent", "id", id)
        );
        return LessonContentConverter.convert(entity, new LessonContentDTO());
    }

}
