package com.hudsontran.courseservice.repository;

import com.hudsontran.courseservice.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    List<CategoryEntity> findAllByIdIn(
            Collection<UUID> ids
    );
}
