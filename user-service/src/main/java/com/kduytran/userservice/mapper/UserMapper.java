package com.kduytran.userservice.mapper;

import com.kduytran.userservice.dto.RegistrationDTO;
import com.kduytran.userservice.dto.UserDTO;
import com.kduytran.userservice.entity.UserEntity;
import com.kduytran.userservice.entity.UserType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static UserEntity convert(RegistrationDTO dto, UserEntity entity) {
        if (entity == null) {
            entity = new UserEntity();
        }
        entity.setEmail(dto.getEmail());
        entity.setName(dto.getName());
        entity.setUserType(UserType.of(dto.getUserType()));
        entity.setMobilePhone(dto.getMobilePhone());
        entity.setUsername(dto.getUsername());
        return entity;
    }

    public static UserDTO convert(UserEntity entity, UserDTO dto) {
        if (dto == null) {
            dto = new UserDTO();
        }
        dto.setId(entity.getId());
        dto.setSeq(entity.getSeq());
        dto.setName(entity.getName());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setUserType(entity.getUserType() != null ? entity.getUserType().getCode() : null);
        dto.setUserStatus(entity.getUserStatus() != null ? entity.getUserStatus().getCode() : null);
        dto.setMobilePhone(entity.getMobilePhone());
        return dto;
    }

}
