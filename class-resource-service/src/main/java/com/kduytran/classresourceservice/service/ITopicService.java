package com.kduytran.classresourceservice.service;

import com.kduytran.classresourceservice.dto.TopicDTO;
import com.kduytran.classresourceservice.dto.UpdateTopicDTO;
import com.kduytran.classresourceservice.dto.UpdateTopicSeqDTO;
import com.kduytran.classresourceservice.entity.EntityStatus;

import java.util.List;
import java.util.UUID;

public interface ITopicService {
    UUID create(UpdateTopicDTO dto);
    void update(UpdateTopicDTO dto);
    void updateSeq(UpdateTopicSeqDTO dto);
    void updateNextSeq(UpdateTopicSeqDTO dto);
    void updatePreviousSeq(UpdateTopicSeqDTO dto);
    void delete(String id);
    List<TopicDTO> findAllByClassId(String classId, List<EntityStatus> statuses);
}
