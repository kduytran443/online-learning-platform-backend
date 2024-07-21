package com.kduytran.classresourceservice.service.impl;

import com.kduytran.classresourceservice.converter.TopicConverter;
import com.kduytran.classresourceservice.dto.TopicDTO;
import com.kduytran.classresourceservice.dto.UpdateTopicDTO;
import com.kduytran.classresourceservice.entity.EntityStatus;
import com.kduytran.classresourceservice.entity.TopicEntity;
import com.kduytran.classresourceservice.repository.TopicRepository;
import com.kduytran.classresourceservice.service.ITopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements ITopicService {

    private final TopicRepository topicRepository;

    @Override
    public UUID create(UpdateTopicDTO dto) {
        TopicEntity topicEntity = TopicConverter.convert(dto, new TopicEntity());

        List<TopicEntity> topics = topicRepository.findAllByClassId(UUID.fromString(dto.getClassId()));
        topicEntity.setSeq(topics.size() + 1);

        topicEntity = topicRepository.save(topicEntity);
        return topicEntity.getId();
    }

    @Override
    public void update(UpdateTopicDTO dto) {
        TopicEntity topic = topicRepository.findById(UUID.fromString(dto.getId())).orElseThrow(
                
        );

    }

    @Override
    public void updateSeq(String id, int seq) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public List<TopicDTO> findAllByClassId(String classId) {
        return null;
    }

    @Override
    public List<TopicDTO> findAllActiveByClassId(String classId) {
        return null;
    }
}
