package com.kduytran.classresourceservice.service.impl;

import com.kduytran.classresourceservice.converter.TopicConverter;
import com.kduytran.classresourceservice.dto.TopicDTO;
import com.kduytran.classresourceservice.dto.UpdateTopicDTO;
import com.kduytran.classresourceservice.dto.UpdateTopicSeqDTO;
import com.kduytran.classresourceservice.entity.EntityStatus;
import com.kduytran.classresourceservice.entity.TopicEntity;
import com.kduytran.classresourceservice.exception.ResourceNotFoundException;
import com.kduytran.classresourceservice.exception.TopicCannotMoveException;
import com.kduytran.classresourceservice.repository.TopicRepository;
import com.kduytran.classresourceservice.service.ITopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements ITopicService {

    private final TopicRepository topicRepository;

    @Override
    public UUID create(UpdateTopicDTO dto) {
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
    public void updateSeq(UpdateTopicSeqDTO dto) {
//        TopicEntity topicEntity = topicRepository.findById(UUID.fromString(dto.getId())).orElseThrow(
//                () -> new ResourceNotFoundException("topic", "id", dto.getId())
//        );
//        topicEntity.setSeq(dto.getSeq());
//
//        List<TopicEntity> topicEntities = topicRepository
//                .findAllByClassIdAndSeqGreaterThanEqualOrderBySeq(UUID.fromString(dto.getClassId()), dto.getSeq());
//
//        topicEntities = topicEntities.stream().map(topic -> {
//            topic.setSeq(topic.getSeq() + 1);
//            return topic;
//        }).collect(Collectors.toList());
//        topicEntities.add(topicEntity);
//
//        topicRepository.saveAll(topicEntities);
    }

    @Override
    public void updateNextSeq(UpdateTopicSeqDTO dto) {
        TopicEntity topicEntity = topicRepository.findById(UUID.fromString(dto.getId())).orElseThrow(
                () -> new ResourceNotFoundException("topic", "id", dto.getId())
        );
        Integer curSeq = topicEntity.getSeq();
        TopicEntity nextTopicEntity = topicRepository
                .findFirstByClassIdAndSeqGreaterThanOrderBySeqAsc(topicEntity.getClassId(), curSeq).orElse(
                        topicRepository.findFirstByClassIdAndSeq(topicEntity.getClassId(), 1)
                );
        if (topicEntity.getId().equals(nextTopicEntity.getId())) {
            throw new TopicCannotMoveException("Cannot move the topic!");
        }
        topicEntity.setSeq(nextTopicEntity.getSeq());
        nextTopicEntity.setSeq(curSeq);
        topicRepository.saveAll(List.of(topicEntity, nextTopicEntity));
    }

    @Override
    public void updatePreviousSeq(UpdateTopicSeqDTO dto) {

    }

    @Override
    public void delete(String id) {
        TopicEntity topicEntity = topicRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("topic", "id", id)
        );
        Integer seq = topicEntity.getSeq();
        topicEntity.setStatus(EntityStatus.DELETED);
        topicEntity.setSeq(null);
        topicRepository.save(topicEntity);

        List<TopicEntity> topicEntities = topicRepository
                .findAllByClassIdAndSeqGreaterThanEqualOrderBySeqAsc(topicEntity.getClassId(), seq);
        topicEntities = topicEntities.stream().filter(topic -> !topic.getId().equals(topic.getId())).map(topic -> {
            topic.setSeq(topic.getSeq() - 1);
            return topic;
        }).collect(Collectors.toList());
        topicRepository.saveAll(topicEntities);
    }

    @Override
    public List<TopicDTO> findAllByClassId(String classId, List<EntityStatus> statuses) {
        List<TopicEntity> topicEntities = topicRepository.findAllByClassIdAndStatusInOrderBySeqAsc(
                UUID.fromString(classId),
                statuses.isEmpty() ? List.of(EntityStatus.LIVE) : statuses
        );
        return topicEntities.stream().map(topic -> TopicConverter
                .convert(topic, new TopicDTO())).collect(Collectors.toList());
    }

}
