package com.kduytran.authmanagementservice.mapper;

import com.kduytran.authmanagementservice.dto.RegistrationDTO;
import com.kduytran.authmanagementservice.entity.SignUpEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public abstract class RegistrationMapper {

    public abstract SignUpEntity map(RegistrationDTO registrationDTO, @MappingTarget SignUpEntity signUpEntity);
}
