package com.kduytran.memberservice.service.impl;

import com.kduytran.memberservice.converter.ClassMemberConverter;
import com.kduytran.memberservice.dto.ClassMemberDTO;
import com.kduytran.memberservice.entity.ClassMemberEntity;
import com.kduytran.memberservice.repository.ClassMemberRepository;
import com.kduytran.memberservice.service.IClassMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassMemberServiceImpl implements IClassMemberService {
    private final ClassMemberRepository classMemberRepository;

    @Override
    public UUID joinClass(ClassMemberDTO dto) {
        ClassMemberEntity entity = ClassMemberConverter.convert(dto, new ClassMemberEntity());
        entity.setJoinedAt(LocalDateTime.now());
        entity = classMemberRepository.save(entity);
        return entity.getId();
    }

}
