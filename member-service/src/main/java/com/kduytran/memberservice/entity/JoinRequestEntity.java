package com.kduytran.memberservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "join_request")
@Getter
@Setter
public class JoinRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private UUID from;

    @Column
    private UUID to;

    @Column
    private JoinRequestType requestType;

    @Column
    private JoinRequestStatus requestStatus;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime decidedAt;
}
