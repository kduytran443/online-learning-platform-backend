package com.kduytran.authmanagementservice.mapper;

import com.kduytran.authmanagementservice.entity.SignUpEntity;
import com.kduytran.authmanagementservice.kafka.message.UserEventMessage;
import org.mapstruct.Mapper;

@Mapper
public abstract class MessageEventMapper {

    public abstract UserEventMessage map(SignUpEntity signUpEntity);
}
