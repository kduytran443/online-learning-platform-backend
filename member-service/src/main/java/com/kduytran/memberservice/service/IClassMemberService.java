package com.kduytran.memberservice.service;

import com.kduytran.memberservice.dto.ClassMemberDTO;

import java.util.UUID;

public interface IClassMemberService {

    UUID joinClass(ClassMemberDTO dto);

}
