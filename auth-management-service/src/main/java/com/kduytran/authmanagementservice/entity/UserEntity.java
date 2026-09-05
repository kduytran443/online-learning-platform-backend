package com.kduytran.authmanagementservice.entity;

import com.kduytran.olpcommon.uuidv7.UUIDv7;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @UUIDv7
    private UUID id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String avatar;

    @Column
    private String oauthId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();
}
