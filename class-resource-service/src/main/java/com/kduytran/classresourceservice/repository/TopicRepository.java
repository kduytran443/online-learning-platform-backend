package com.kduytran.classresourceservice.repository;

import com.kduytran.classresourceservice.entity.EntityStatus;
import com.kduytran.classresourceservice.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, UUID> {

    List<TopicEntity> findAllByClassIdAndStatusInOrderBySeqAsc(UUID classId, List<EntityStatus> statuses);

    long countAllByClassIdAndStatusIn(UUID classId, List<EntityStatus> statuses);
    List<TopicEntity> findAllByClassIdOrderBySeq(UUID classId);
    List<TopicEntity> findAllByClassIdAndSeqGreaterThanEqualOrderBySeqAsc(UUID classId, Integer seq);
    Optional<TopicEntity> findFirstByClassIdAndSeqGreaterThanOrderBySeqAsc(UUID classId, Integer seq);
    TopicEntity findFirstByClassIdAndSeq(UUID classId, Integer seq);
    Optional<TopicEntity> findFirstByClassIdAndSeqLessThanOrderBySeqDesc(UUID classId, Integer seq);

}
