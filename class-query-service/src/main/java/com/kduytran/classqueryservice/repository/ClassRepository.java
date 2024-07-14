package com.kduytran.classqueryservice.repository;

import com.kduytran.classqueryservice.entity.ClassEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, UUID> {
    List<ClassEntity> findAllByStatusAndEndAtIsAfter(String status, LocalDateTime endAt);
    boolean existsById(UUID id);

    Page<ClassEntity> findByStatusAndEndAtIsAfterAndAverageRatingBetweenAndCategoryIdIn(
            String status, LocalDateTime endAt,
            Double minAverageRating, Double maxAverageRating,
            List<String> categories, Pageable pageable);

}
