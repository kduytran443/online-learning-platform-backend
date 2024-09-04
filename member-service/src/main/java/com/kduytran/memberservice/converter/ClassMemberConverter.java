package com.kduytran.memberservice.converter;

import com.kduytran.memberservice.dto.ClassMemberDTO;
import com.kduytran.memberservice.entity.ClassMemberEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ClassMemberConverter {

    public static ClassMemberEntity convert(ClassMemberDTO dto, ClassMemberEntity entity) {
        if (entity == null) {
            entity = new ClassMemberEntity();
        }
        entity.setClassId(dto.getClassId());
        entity.setUserId(dto.getUserId());
        entity.setRole(dto.getRole());
        return entity;
    }

}
