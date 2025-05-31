package com.kduytran.memberservice.repository;

import com.kduytran.memberservice.entity.ClassMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface ClassMemberRepository extends JpaRepository<ClassMemberEntity, UUID> {

    @Query("SELECT classId FROM ClassMemberEntity WHERE userId = :userId")
    Set<UUID> findAllClassIdsByUserId(UUID userId);
}
