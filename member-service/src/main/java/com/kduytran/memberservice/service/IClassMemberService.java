package com.kduytran.memberservice.service;

import com.kduytran.memberservice.dto.ClassMemberRequestDTO;

import java.util.UUID;

public interface IClassMemberService {

    UUID joinClass(ClassMemberRequestDTO dto);

}
