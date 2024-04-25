package com.kduytran.userservice.repository;

import com.kduytran.userservice.entity.UserVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerificationEntity, String> {

}
