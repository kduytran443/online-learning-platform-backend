package com.kduytran.userservice.repository;

import com.kduytran.userservice.entity.UserEntity;
import com.kduytran.userservice.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByMobilePhone(String mobilePhone);

    Optional<UserEntity> findByUserStatusAndId(UserStatus userStatus, UUID id);

}
