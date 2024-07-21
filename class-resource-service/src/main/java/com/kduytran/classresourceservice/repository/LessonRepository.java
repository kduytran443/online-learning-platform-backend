package com.kduytran.classresourceservice.repository;

import com.kduytran.classresourceservice.entity.EntityStatus;
import com.kduytran.classresourceservice.entity.LessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<LessonEntity, UUID> {

    List<LessonEntity> findAllByTopicIdAndStatusInOrderBySeq(UUID topicId, List<EntityStatus> status);

}