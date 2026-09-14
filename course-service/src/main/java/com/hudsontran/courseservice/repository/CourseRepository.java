package com.hudsontran.courseservice.repository;

import com.hudsontran.courseservice.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<CourseEntity, UUID> {

    boolean existsBySlug(String slug);
    List<CourseEntity> findByAuthorId(UUID authorId);
}
