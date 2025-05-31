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
@NoArgsConstructor
public class ClassCreatedApplicationEvent extends AbstractClassApplicationEvent {

    public ClassCreatedApplicationEvent(String transactionId, UUID id, String name, String image,
                                        boolean hasPassword, LocalDateTime startAt, LocalDateTime endAt,
                                        String status, UUID categoryId, String ownerType, UUID ownerId) {
        super(transactionId, id, name, image, hasPassword, startAt, endAt, status, categoryId, ownerType, ownerId);
    }

    @Override
    public Action getAction() {
        return Action.CREATE;
    }

}
