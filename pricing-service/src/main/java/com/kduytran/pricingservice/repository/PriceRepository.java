package com.kduytran.pricingservice.repository;

import com.kduytran.pricingservice.entity.EntityStatus;
import com.kduytran.pricingservice.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PriceRepository extends JpaRepository<PriceEntity, UUID> {

    List<PriceEntity> findAllByTargetIdAndStatusOrderByCreatedAtDesc(UUID targetId, EntityStatus status);
    List<PriceEntity> findAllByTargetIdOrderByCreatedAtDesc(UUID targetId);
    Optional<PriceEntity> findFirstByTargetIdOrderByCreatedAtDesc(UUID targetId);

}
