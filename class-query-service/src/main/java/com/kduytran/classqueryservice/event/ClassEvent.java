package com.kduytran.classqueryservice.event;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ClassEvent {
    private String transactionId;
    private Action action;
    private String id;
    private String name;
    private String image;
    private boolean hasPassword;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String status;
    private UUID categoryId;
    private String ownerType;
    private UUID ownerId;
}
