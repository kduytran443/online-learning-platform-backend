package com.kduytran.authmanagementservice.repository;

import com.kduytran.authmanagementservice.entity.SignUpEntity;
import com.kduytran.authmanagementservice.entity.SignUpStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SignUpRepository extends JpaRepository<SignUpEntity, UUID> {

    boolean existsByUsernameAndStatusIn(String username, List<SignUpStatus> statuses);

    boolean existsByEmailAndStatusIn(String email, List<SignUpStatus> statuses);

    boolean existsByMobilePhoneAndStatusIn(String mobilePhone, List<SignUpStatus> statuses);

    Optional<SignUpEntity> findByCurrentVerificationTokenAndStatusIn(String token, List<SignUpStatus> statuses);

    Optional<SignUpEntity> findByUsernameAndStatusIn(String username, List<SignUpStatus> statuses);
}
