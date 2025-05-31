package com.kduytran.memberservice.service.impl;

import com.kduytran.memberservice.ClassMemberSizeMapper;
import com.kduytran.memberservice.dto.ClassMemberSizeDTO;
import com.kduytran.memberservice.entity.ClassMemberSizeEntity;
import com.kduytran.memberservice.exception.MemberMaxSizeExceededException;
import com.kduytran.memberservice.exception.MemberSizeUnderflowException;
import com.kduytran.memberservice.exception.ResourceNotFoundException;
import com.kduytran.memberservice.repository.ClassMemberSizeRepository;
import com.kduytran.memberservice.service.ClassMemberSizeService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
class ClassMemberSizeServiceImpl implements ClassMemberSizeService {

    private final ClassMemberSizeRepository classMemberSizeRepository;
    private final ClassMemberSizeMapper classMemberSizeMapper;

    @Override
    public ClassMemberSizeDTO getClassSize(UUID classId) {
        ClassMemberSizeEntity memberSize = getByClassId(classId);
        return classMemberSizeMapper.convert(memberSize);
    }

    @Override
    public void updateClassMemberSize(@Valid ClassMemberSizeDTO dto) {
        ClassMemberSizeEntity memberSize = classMemberSizeRepository.findByClassId(dto.getClassId())
                .orElse(new ClassMemberSizeEntity());
        memberSize.setClassId(dto.getClassId());
        memberSize.setCurrentSize(dto.getCurrentSize());
        memberSize.setMaxSize(dto.getMaxSize());
        classMemberSizeRepository.save(memberSize);
    }

    @Override
    public void updateSizeOnly(UUID classId, int maxSize) {
        ClassMemberSizeEntity memberSize = getByClassId(classId);
        memberSize.setMaxSize(maxSize);
        classMemberSizeRepository.save(memberSize);
    }

    @Override
    public void increaseCurrentSize(UUID classId) {
        // Pessimistic locking to check max size
        ClassMemberSizeEntity memberSize = classMemberSizeRepository.findByClassIdForUpdate(classId).orElseThrow(
                () -> new ResourceNotFoundException("ClassMember", "classId", classId.toString())
        );
        if (memberSize.getCurrentSize() >= memberSize.getMaxSize()) {
            throw new MemberMaxSizeExceededException("The class has reached its maximum member size",
                    memberSize.getMaxSize());
        }
        classMemberSizeRepository.increaseCurrentSize(classId);
    }

    @Override
    public void decreaseCurrentSize(UUID classId) {
        // Pessimistic locking to check current size
        ClassMemberSizeEntity memberSize = classMemberSizeRepository.findByClassIdForUpdate(classId)
                .orElseThrow(() -> new ResourceNotFoundException("ClassMember", "classId", classId.toString()));
        if (memberSize.getCurrentSize() <= 0) {
            throw new MemberSizeUnderflowException("Cannot decrease current size as it is already zero or negative");
        }
        classMemberSizeRepository.decreaseCurrentSize(classId);
    }

    private ClassMemberSizeEntity getByClassId(UUID classId) {
        return classMemberSizeRepository.findByClassId(classId).orElseThrow(
                () -> new ResourceNotFoundException("ClassMember", "classId", classId.toString())
        );
    }
}
