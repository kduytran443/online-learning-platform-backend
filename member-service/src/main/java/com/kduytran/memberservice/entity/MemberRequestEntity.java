package com.kduytran.memberservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "MB_MEMBER_REQUEST")
@Getter
@Setter
public class MemberRequestEntity {

    @Id
    private UUID id;

    @Column
    private UUID fromId;

    @Column
    private UUID toId;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberRequestType requestType;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberRequestStatus requestStatus;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime decidedAt;

    @PrePersist
    private void prePersis() {
        if (getId() == null) {
            setId(UUID.randomUUID());
        }
    }
}
