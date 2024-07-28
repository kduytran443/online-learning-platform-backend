package com.kduytran.classresourceservice.service.impl;

import com.kduytran.classresourceservice.converter.TopicConverter;
import com.kduytran.classresourceservice.dto.*;
import com.kduytran.classresourceservice.entity.EntityStatus;
import com.kduytran.classresourceservice.entity.TopicEntity;
import com.kduytran.classresourceservice.exception.CannotMoveException;
import com.kduytran.classresourceservice.exception.ResourceNotFoundException;
import com.kduytran.classresourceservice.exception.TopicLengthNotValidException;
import com.kduytran.classresourceservice.repository.TopicRepository;
import com.kduytran.classresourceservice.service.ILessonService;
import com.kduytran.classresourceservice.service.ITopicService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl implements ITopicService {
    private final TopicRepository topicRepository;
    private final ILessonService lessonService;

    public TopicServiceImpl(TopicRepository topicRepository, @Lazy ILessonService lessonService) {
        this.topicRepository = topicRepository;
        this.lessonService = lessonService;
    }

    @Override
    public UUID create(CreateTopicDTO dto) {
        TopicEntity topicEntity = TopicConverter.convert(dto, new TopicEntity());
        long topicSize = topicRepository.countAllByClassIdAndStatusIn(UUID.fromString(dto.getClassId()),
                List.of(EntityStatus.LIVE, EntityStatus.HIDDEN));
        topicEntity.setSeq((int) (topicSize + 1));

        topicEntity = topicRepository.save(topicEntity);
        return topicEntity.getId();
    }

    @Override
    public void update(UpdateTopicDTO dto) {
        TopicEntity topic = topicRepository.findById(UUID.fromString(dto.getId())).orElseThrow(
                () -> new ResourceNotFoundException("topic", "id", dto.getId())
        );
        topic = TopicConverter.convert(dto, topic);
        topicRepository.save(topic);
    }

    @Override
    public void updateNextSeq(String id) {
        TopicEntity topicEntity = topicRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("topic", "id", id)
        );
        getActiveTopicLength(topicEntity.getClassId(), 1L, null);
        Integer curSeq = topicEntity.getSeq();
        TopicEntity nextTopicEntity = topicRepository
                .findFirstByClassIdAndSeqGreaterThanOrderBySeqAsc(topicEntity.getClassId(), curSeq).orElse(
                        topicRepository.findFirstByClassIdAndSeq(topicEntity.getClassId(), 1)
                );
        if (topicEntity.getId().equals(nextTopicEntity.getId())) {
            throw new CannotMoveException("Cannot move the topic!");
        }
        topicEntity.setSeq(nextTopicEntity.getSeq());
        nextTopicEntity.setSeq(curSeq);
        topicRepository.saveAll(List.of(topicEntity, nextTopicEntity));
    }

    @Override
    public void updatePreviousSeq(String id) {
        TopicEntity topicEntity = topicRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("topic", "id", id)
        );
        long topicLength = getActiveTopicLength(topicEntity.getClassId(), 1L, null);
        TopicEntity prevTopicEntity = topicRepository
                .findFirstByClassIdAndSeqLessThanOrderBySeqDesc(topicEntity.getClassId(), topicEntity.getSeq()).orElse(
                        topicRepository.findFirstByClassIdAndSeq(topicEntity.getClassId(), (int) topicLength)
                );
        if (topicEntity.getId().equals(prevTopicEntity.getId())) {
            throw new CannotMoveException("Cannot move the topic!");
        }
        Integer curSeq = topicEntity.getSeq();
        topicEntity.setSeq(prevTopicEntity.getSeq());
        prevTopicEntity.setSeq(curSeq);
        topicRepository.saveAll(List.of(topicEntity, prevTopicEntity));
    }

    public long getActiveTopicLength(UUID id, Long minLength, Long maxLength) {
        long topicLength = topicRepository.countAllByClassIdAndStatusIn((id),
                List.of(EntityStatus.LIVE, EntityStatus.HIDDEN));
        if ((minLength != null && topicLength < minLength) || (maxLength != null && topicLength > maxLength)) {
            throw new TopicLengthNotValidException("Invalid topic length!");
        }
        return topicLength;
    }

    @Override
    public void delete(String id) {
        TopicEntity topicEntity = topicRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("topic", "id", id)
        );
        Integer seq = topicEntity.getSeq();
        topicEntity.setStatus(EntityStatus.DELETED);
        topicEntity.setSeq(null);

        List<TopicEntity> topicEntities = topicRepository
                .findAllByClassIdAndSeqGreaterThanEqualOrderBySeqAsc(topicEntity.getClassId(), seq + 1);
        topicEntities = topicEntities.stream().map(topic -> {
            topic.setSeq(topic.getSeq() - 1);
            return topic;
        }).collect(Collectors.toList());
        topicEntities.add(topicEntity);
        topicRepository.saveAll(topicEntities);
    }

    @Override
    public void hide(String id) {
        TopicEntity topicEntity = topicRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("topic", "id", id)
        );
        topicEntity.setStatus(EntityStatus.HIDDEN);
        topicRepository.save(topicEntity);
    }

    @Override
    public List<TopicDTO> findAllByClassId(String classId, List<EntityStatus> statuses) {
        List<TopicEntity> topicEntities = topicRepository.findAllByClassIdAndStatusInOrderBySeqAsc(
                UUID.fromString(classId),
                statuses.isEmpty() ? List.of(EntityStatus.LIVE) : statuses
        );
        return topicEntities.stream().map(topic -> TopicConverter
                .convert(topic, new TopicDTO())).map(this::setLessons).collect(Collectors.toList());
    }

    @Override
    public TopicDTO getTopicDetailsById(String id) {
        TopicDTO topicDTO = getTopicById(id);
        setLessons(topicDTO);
        return topicDTO;
    }

    @Override
    public TopicDTO getTopicById(String id) {
        TopicEntity topicEntity = topicRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("topic", "id", id)
        );
        return TopicConverter.convert(topicEntity, new TopicDTO());
    }

    private TopicDTO setLessons(TopicDTO topicDTO) {
        List<LessonDTO> lessonDTOs = lessonService.findAllByTopicId(topicDTO.getId(),
                List.of(EntityStatus.LIVE, EntityStatus.HIDDEN));
        topicDTO.setLessons(lessonDTOs);
        return topicDTO;
    }

}
