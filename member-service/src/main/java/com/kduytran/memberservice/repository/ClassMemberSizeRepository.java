package com.kduytran.memberservice.repository;

import com.kduytran.memberservice.entity.ClassMemberSizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClassMemberSizeRepository extends JpaRepository<ClassMemberSizeEntity, Long> {

    Optional<ClassMemberSizeEntity> findByClassId(UUID classId);
}
