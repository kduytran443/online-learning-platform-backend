package com.kduytran.classresourceservice.service;

import com.kduytran.classresourceservice.dto.TopicDTO;
import com.kduytran.classresourceservice.dto.UpdateTopicDTO;

import java.util.List;
import java.util.UUID;

public interface ITopicService {
    UUID create(UpdateTopicDTO dto);
    void update(UpdateTopicDTO dto);
    void updateSeq(String id, int seq);
    void delete(String id);
    List<TopicDTO> findAllByClassId(String classId);
    List<TopicDTO> findAllActiveByClassId(String classId);
}
