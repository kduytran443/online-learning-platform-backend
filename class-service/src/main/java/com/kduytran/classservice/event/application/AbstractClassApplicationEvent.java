package com.kduytran.classservice.event.application;

import com.kduytran.classservice.entity.EntityStatus;
import com.kduytran.classservice.entity.OwnerType;
import com.kduytran.classservice.event.Action;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractClassApplicationEvent {
    private String transactionId;
    private UUID id;
    private String name;
    private String image;
    private boolean hasPassword;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String status;
    private UUID categoryId;
    private String ownerType;
    private UUID ownerId;

    public abstract Action getAction();

}
