package com.kduytran.classresourceservice.repository;

import com.kduytran.classresourceservice.entity.AssignmentEntity;
import com.kduytran.classresourceservice.entity.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AssignmentRepository extends JpaRepository<AssignmentEntity, UUID> {

    List<AssignmentEntity> findAllByTopicIdAndStatusInOrderBySeq(UUID topicId, List<EntityStatus> status);

}