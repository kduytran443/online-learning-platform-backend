package com.kduytran.classresourceservice.repository;

import com.kduytran.classresourceservice.entity.LessonContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LessonContentRepository extends JpaRepository<LessonContentEntity, UUID> {

}
