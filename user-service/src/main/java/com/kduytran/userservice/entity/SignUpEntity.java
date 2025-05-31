package com.kduytran.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity(name = "sign_up")
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

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String mobilePhone;

    @Column
    private UserType userType;

    @Column
    private LocalDateTime createdAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "signUp", cascade = CascadeType.PERSIST)
    private List<UserVerificationEntity> userVerifications;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
