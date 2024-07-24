package com.kduytran.classresourceservice.service;

import com.kduytran.classresourceservice.dto.CreateTopicDTO;
import com.kduytran.classresourceservice.dto.TopicDTO;
import com.kduytran.classresourceservice.dto.UpdateTopicDTO;
import com.kduytran.classresourceservice.entity.EntityStatus;

import java.util.List;
import java.util.UUID;

public interface ITopicService {
    UUID create(CreateTopicDTO dto);
    void update(UpdateTopicDTO dto);
    void updateNextSeq(String id);
    void updatePreviousSeq(String id);
    void delete(String id);
    void hide(String id);
    List<TopicDTO> findAllByClassId(String classId, List<EntityStatus> statuses);
    TopicDTO getTopicDetailsById(String id);
}
