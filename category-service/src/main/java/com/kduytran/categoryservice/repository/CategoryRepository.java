package com.kduytran.categoryservice.repository;

import com.kduytran.categoryservice.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>,
        JpaSpecificationExecutor<CategoryEntity> {

    Optional<CategoryEntity> findOneByCode(String code);

    Optional<CategoryEntity> findOneById(String id);

    boolean existsById(String id);

    @Modifying
    @Query("UPDATE category c SET c.deleted = false WHERE c.id = :id")
    void restoreById(@Param("id") String id);
}
