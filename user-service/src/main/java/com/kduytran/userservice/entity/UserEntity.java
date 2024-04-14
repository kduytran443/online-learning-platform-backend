package com.kduytran.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "userdata")
@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class UserEntity extends BaseEntity {

    @Column
    private Long seq;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String username;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private UserType userType;

    @Column
    private String mobilePhone;

}
