package com.kduytran.classresourceservice.repository;

import com.kduytran.classresourceservice.entity.EntityStatus;
import com.kduytran.classresourceservice.entity.LessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<LessonEntity, UUID> {

    List<LessonEntity> findAllByTopicIdAndStatusInOrderBySeqAsc(UUID topicId, List<EntityStatus> status);
    long countAllByTopicIdAndStatusIn(UUID topicId, List<EntityStatus> statuses);
    List<LessonEntity> findAllByTopicIdAndSeqGreaterThanEqualOrderBySeqAsc(UUID topicId, Integer seq);
    Optional<LessonEntity> findFirstByTopicIdAndSeqGreaterThanOrderBySeqAsc(UUID topicId, Integer seq);
    LessonEntity findFirstByTopicIdAndSeq(UUID topicId, Integer seq);

}