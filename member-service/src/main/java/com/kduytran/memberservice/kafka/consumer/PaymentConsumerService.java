package com.kduytran.memberservice.kafka.consumer;

import com.kduytran.memberservice.dto.ClassMemberRequestDTO;
import com.kduytran.memberservice.event.PaymentEvent;
import com.kduytran.memberservice.event.PaymentEventType;
import com.kduytran.memberservice.service.IClassMemberService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentConsumerService {

    private final IClassMemberService classMemberService;
    private final ModelMapper modelMapper;

    public void handle(@NotNull PaymentEvent event) {
        if (Objects.requireNonNull(event.getAction()) == PaymentEventType.EXECUTE) {
            classMemberService.joinClass(modelMapper.map(event, ClassMemberRequestDTO.class));
        } else {
            log.debug("Unhanding action: {}", event.getAction());
        }
    }
}
