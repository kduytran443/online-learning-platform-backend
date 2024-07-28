package com.kduytran.classresourceservice.service.impl;

import com.kduytran.classresourceservice.converter.LessonContentConverter;
import com.kduytran.classresourceservice.converter.LessonConverter;
import com.kduytran.classresourceservice.converter.TopicConverter;
import com.kduytran.classresourceservice.dto.*;
import com.kduytran.classresourceservice.entity.EntityStatus;
import com.kduytran.classresourceservice.entity.LessonEntity;
import com.kduytran.classresourceservice.entity.TopicEntity;
import com.kduytran.classresourceservice.exception.CannotDeleteException;
import com.kduytran.classresourceservice.exception.CannotMoveException;
import com.kduytran.classresourceservice.exception.LessonLengthNotValidException;
import com.kduytran.classresourceservice.exception.ResourceNotFoundException;
import com.kduytran.classresourceservice.repository.LessonRepository;
import com.kduytran.classresourceservice.repository.TopicRepository;
import com.kduytran.classresourceservice.service.ILessonService;
import com.kduytran.classresourceservice.service.ITopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements ILessonService {
    private final LessonRepository lessonRepository;
    private final TopicRepository topicRepository;
    private final ITopicService topicService;

    @Override
    public UUID create(CreateLessonDTO dto) {
        TopicEntity topicEntity = topicRepository.findById(UUID.fromString(dto.getTopicId())).orElseThrow(
                () -> new ResourceNotFoundException("topic", "id", dto.getTopicId())
        );
        LessonEntity lessonEntity = LessonConverter.convert(dto, new LessonEntity());
        lessonEntity.setTopic(topicEntity);
        long lessonSize = lessonRepository.countAllByTopicIdAndStatusIn(topicEntity.getId(),
                List.of(EntityStatus.LIVE, EntityStatus.HIDDEN));
        lessonEntity.setSeq((int) (lessonSize + 1));
        lessonEntity = lessonRepository.save(lessonEntity);
        return lessonEntity.getId();
    }

    @Override
    public void update(UpdateLessonDTO dto) {
        LessonEntity lessonEntity = lessonRepository.findById(UUID.fromString(dto.getId())).orElseThrow(
                () -> new ResourceNotFoundException("lesson", "id", dto.getId())
        );
        lessonEntity = LessonConverter.convert(dto, lessonEntity);
        lessonRepository.save(lessonEntity);
    }

    @Override
    public void delete(String id) {
        LessonEntity lessonEntity = lessonRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("lesson", "id", id)
        );
        if (lessonEntity.getStatus() == EntityStatus.DELETED || lessonEntity.getSeq() == null) {
            throw new CannotDeleteException("Cannot delete the lesson!");
        }
        Integer curSeq = lessonEntity.getSeq();
        lessonEntity.setStatus(EntityStatus.DELETED);
        lessonEntity.setSeq(null);

        List<LessonEntity> lessonEntities = lessonRepository.findAllByTopicIdAndSeqGreaterThanEqualOrderBySeqAsc(
                lessonEntity.getTopic().getId(),
                curSeq + 1
        );
        lessonEntities = lessonEntities.stream().map(lesson -> {
            lesson.setSeq(lesson.getSeq() + 1);
            return lesson;
        }).collect(Collectors.toList());
        lessonEntities.add(lessonEntity);
        lessonRepository.saveAll(lessonEntities);
    }

    @Override
    public void updateNextSeq(String id) {
        LessonEntity lessonEntity = lessonRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("lesson", "id", id)
        );
        TopicEntity topicEntity = lessonEntity.getTopic();
        getActiveLessonLength(topicEntity.getId(), 1L, null);
        Integer curSeq = lessonEntity.getSeq();
        LessonEntity nextLessonEntity = lessonRepository.findFirstByTopicIdAndSeqGreaterThanOrderBySeqAsc(
                topicEntity.getId(), curSeq)
                .orElse(lessonRepository.findFirstByTopicIdAndSeq(topicEntity.getId(), 1));
        if (lessonEntity.getId().equals(nextLessonEntity.getId())) {
            throw new CannotMoveException("Cannot move the lesson!");
        }
        lessonEntity.setSeq(nextLessonEntity.getSeq());
        nextLessonEntity.setSeq(curSeq);
        lessonRepository.saveAll(List.of(lessonEntity, nextLessonEntity));
    }

    @Override
    public void updatePreviousSeq(String id) {
        LessonEntity lessonEntity = lessonRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("lesson", "id", id)
        );
        TopicEntity topicEntity = lessonEntity.getTopic();
        long lessonLength = getActiveLessonLength(topicEntity.getId(), 1L, null);
        Integer curSeq = lessonEntity.getSeq();
        LessonEntity prevLessonEntity = lessonRepository.findFirstByTopicIdAndSeqLessThanOrderBySeqDesc(
                        topicEntity.getId(), curSeq)
                .orElse(lessonRepository.findFirstByTopicIdAndSeq(topicEntity.getId(), (int) lessonLength));
        if (prevLessonEntity == null || lessonEntity.getId().equals(prevLessonEntity.getId())) {
            throw new CannotMoveException("Cannot move the lesson!");
        }
        lessonEntity.setSeq(prevLessonEntity.getSeq());
        prevLessonEntity.setSeq(curSeq);
        lessonRepository.saveAll(List.of(lessonEntity, prevLessonEntity));
    }

    @Override
    public void hide(String id) {
        LessonEntity lessonEntity = lessonRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("lesson", "id", id)
        );
        lessonEntity.setStatus(EntityStatus.HIDDEN);
        lessonRepository.save(lessonEntity);
    }

    @Override
    public List<LessonDTO> findAllByTopicId(String topicId, List<EntityStatus> statuses) {
        List<LessonEntity> lessonEntities = lessonRepository.findAllByTopicIdAndStatusInOrderBySeqAsc(
                UUID.fromString(topicId),
                List.of(EntityStatus.LIVE, EntityStatus.HIDDEN)
        );
        TopicDTO topicDTO = topicService.getTopicById(topicId);
        return lessonEntities.stream()
                .map(lesson -> {
                    LessonDTO lessonDTO = LessonConverter.convert(lesson, new LessonDTO());
                    lessonDTO.setTopic(topicDTO);
                    return lessonDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public LessonDTO getLessonDetailsById(String id) {
        LessonEntity lessonEntity = lessonRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("lesson", "id", id)
        );
        LessonDTO lessonDTO = LessonConverter.convert(lessonEntity, new LessonDTO());
        TopicDTO topicDTO = TopicConverter.convert(lessonEntity.getTopic(), new TopicDTO());
        LessonContentDTO lessonContentDTO = LessonContentConverter.convert(lessonEntity.getLessonContent(),
                new LessonContentDTO());
        lessonDTO.setTopic(topicDTO);
        lessonDTO.setContent(lessonContentDTO);
        return lessonDTO;
    }

    public long getActiveLessonLength(UUID id, Long minLength, Long maxLength) {
        long lessonLength = lessonRepository.countAllByTopicIdAndStatusIn(id,
                List.of(EntityStatus.LIVE, EntityStatus.HIDDEN));
        if ((minLength != null && lessonLength < minLength) || (maxLength != null && lessonLength > maxLength)) {
            throw new LessonLengthNotValidException("Invalid topic length!");
        }
        return lessonLength;
    }

}
