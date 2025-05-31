package com.kduytran.memberservice;

import com.kduytran.memberservice.dto.ClassMemberSizeDTO;
import com.kduytran.memberservice.entity.ClassMemberSizeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ClassMemberSizeMapper {

    public abstract ClassMemberSizeDTO convert(ClassMemberSizeEntity entity);
}
