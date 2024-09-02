package com.kduytran.orderservice.repository;

import com.kduytran.orderservice.entity.OrderDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetailsEntity, UUID> {
}
