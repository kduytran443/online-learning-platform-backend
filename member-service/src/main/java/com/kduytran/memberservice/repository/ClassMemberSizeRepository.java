package com.kduytran.memberservice.repository;

import com.kduytran.memberservice.entity.ClassMemberSizeEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ClassMemberSizeRepository extends JpaRepository<ClassMemberSizeEntity, Long> {

    Optional<ClassMemberSizeEntity> findByClassId(UUID classId);

    @Modifying
    @Query("UPDATE ClassMemberSizeEntity c SET c.currentSize = c.currentSize + 1 WHERE c.classId = :classId")
    void increaseCurrentSize(@Param("classId") UUID classId);

    @Modifying
    @Query("UPDATE ClassMemberSizeEntity c SET c.currentSize = c.currentSize - 1 where c.classId = :classId")
    void decreaseCurrentSize(@Param("classId") UUID classId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM ClassMemberSizeEntity c WHERE c.classId = :classId")
    Optional<ClassMemberSizeEntity> findByClassIdForUpdate(UUID classId);
}
