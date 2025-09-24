package com.kduytran.authmanagementservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "auth_sign_up")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String username;

    @Column(columnDefinition = "TEXT")
    private String password;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String mobilePhone;

    @Column
    private Instant createdAt;

    @Column
    @Enumerated(EnumType.STRING)
    private SignUpStatus status;

    @Column
    private String currentVerificationToken;

    @Column
    private Instant expiredVerificationTokenDate;
}
