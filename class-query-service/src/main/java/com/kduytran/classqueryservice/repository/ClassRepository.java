package com.kduytran.classqueryservice.repository;

import com.kduytran.classqueryservice.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, UUID> {
    List<ClassEntity> findAllByStatusAndEndAtIsAfter(String status, LocalDateTime endAt);
    boolean existsById(UUID id);

}
