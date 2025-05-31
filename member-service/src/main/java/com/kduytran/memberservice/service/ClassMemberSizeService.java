package com.kduytran.memberservice.service;

import com.kduytran.memberservice.dto.ClassMemberSizeDTO;
import jakarta.validation.Valid;

import java.util.UUID;

public interface ClassMemberSizeService {

    ClassMemberSizeDTO getClassSize(UUID classId);

    void updateClassMemberSize(@Valid ClassMemberSizeDTO classMemberSize);

    void updateSizeOnly(UUID classId, int maxSize);

    void increaseCurrentSize(UUID classId);

    void decreaseCurrentSize(UUID classId);
}
