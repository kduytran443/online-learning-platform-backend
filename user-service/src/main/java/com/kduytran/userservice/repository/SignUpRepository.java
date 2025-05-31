package com.kduytran.userservice.repository;

import com.kduytran.userservice.entity.SignUpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SignUpRepository extends JpaRepository<SignUpEntity, UUID> {

}
