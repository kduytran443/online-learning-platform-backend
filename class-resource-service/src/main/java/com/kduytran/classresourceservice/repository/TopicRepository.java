package com.kduytran.classresourceservice.repository;

import com.kduytran.classresourceservice.entity.EntityStatus;
import com.kduytran.classresourceservice.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, UUID> {

    List<TopicEntity> findAllByClassIdAndStatusInOrderBySeqSeqAsc(EntityStatus status, UUID classId);
    List<TopicEntity> findAllByClassId(UUID classId);

}
