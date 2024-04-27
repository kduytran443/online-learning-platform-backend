package com.kduytran.userservice.repository;

import com.kduytran.userservice.entity.UserEntity;
import com.kduytran.userservice.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByMobilePhone(String mobilePhone);

    Optional<UserEntity> findByUserStatusAndId(UserStatus userStatus, UUID id);

    @Transactional
    @Modifying(flushAutomatically = true)
    @Query(value = "DELETE FROM userdata ud " +
            "WHERE NOT EXISTS ( " +
            "    SELECT 1 FROM user_verification uv " +
            "    WHERE uv.user_id = ud.id AND uv.expired_date > NOW() " +
            ");",
            nativeQuery = true)
    int deleteAllInActiveUsers();

}
