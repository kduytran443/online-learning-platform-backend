package com.kduytran.categoryservice.repository;

import com.kduytran.categoryservice.entity.CategoryEntity;
import com.kduytran.categoryservice.entity.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    Optional<List<CategoryEntity>> findAllByParentCategory(CategoryEntity parentCategory);
    Optional<CategoryEntity> findByIdAndStatusIn(UUID id, List<EntityStatus> status);
    Optional<CategoryEntity> findByIdAndStatus(UUID id, EntityStatus status);
    Optional<CategoryEntity> findByCodeAndStatus(String code, EntityStatus status);
    boolean existsByIdOrCode(UUID id, String code);

}
