package com.kduytran.pricingservice.service.impl;

import com.kduytran.pricingservice.converter.PriceConverter;
import com.kduytran.pricingservice.dto.CreatePriceDTO;
import com.kduytran.pricingservice.dto.PriceDTO;
import com.kduytran.pricingservice.entity.EntityStatus;
import com.kduytran.pricingservice.entity.PriceEntity;
import com.kduytran.pricingservice.exception.ResourceNotFoundException;
import com.kduytran.pricingservice.repository.PriceRepository;
import com.kduytran.pricingservice.service.IPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements IPriceService {
    private final PriceRepository priceRepository;

    @Override
    @Transactional
    public UUID createPrice(CreatePriceDTO dto) {
        PriceEntity newEntity = PriceConverter.convert(dto, new PriceEntity());
        LocalDateTime curTime = LocalDateTime.now();
        List<PriceEntity> priceEntityList = priceRepository
                .findAllByTargetIdAndStatusOrderByCreatedAtDesc(dto.getTargetId(), EntityStatus.ACTIVE)
                .stream().map(entity -> {
                    entity.setStatus(EntityStatus.CLOSED);
                    entity.setClosedAt(curTime);
                    entity.setCloseReason(dto.getCloseReason());
                    return entity;
                }).collect(Collectors.toList());
        priceRepository.saveAll(priceEntityList);

        newEntity = priceRepository.save(newEntity);
        return newEntity.getId();
    }

    @Override
    public List<PriceDTO> getAllByTargetId(String targetId) {
        return priceRepository
                .findAllByTargetIdOrderByCreatedAtDesc(UUID.fromString(targetId))
                .stream()
                .map(entity -> PriceConverter.convert(entity, new PriceDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public PriceDTO getOneByTargetId(String targetId) {
        PriceEntity priceEntity = priceRepository.findFirstByTargetIdOrderByCreatedAtDesc(UUID.fromString(targetId))
                .orElseThrow(() -> new ResourceNotFoundException("price", "targetId", targetId));
        return PriceConverter.convert(priceEntity, new PriceDTO());
    }

}
