package com.kduytran.userservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "user_verification")
public class UserVerificationEntity {

    @Id
    @Column
    private String token;

    @Column(updatable = false)
    private LocalDateTime expiredDate;

    @Column
    private boolean checked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
