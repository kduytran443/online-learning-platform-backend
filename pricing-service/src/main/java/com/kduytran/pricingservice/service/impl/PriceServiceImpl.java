package com.kduytran.pricingservice.service.impl;

import com.kduytran.pricingservice.converter.PriceConverter;
import com.kduytran.pricingservice.dto.CreatePriceDTO;
import com.kduytran.pricingservice.dto.PriceDTO;
import com.kduytran.pricingservice.entity.EntityStatus;
import com.kduytran.pricingservice.entity.PriceEntity;
import com.kduytran.pricingservice.event.AbstractPriceEvent;
import com.kduytran.pricingservice.event.PriceCreatedEvent;
import com.kduytran.pricingservice.exception.ResourceNotFoundException;
import com.kduytran.pricingservice.exception.SamePriceException;
import com.kduytran.pricingservice.repository.PriceRepository;
import com.kduytran.pricingservice.service.IPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public UUID createPrice(CreatePriceDTO dto) {
        LocalDateTime curTime = LocalDateTime.now();
        List<PriceEntity> priceEntityList = priceRepository
                .findAllByTargetIdAndStatusOrderByCreatedAtDesc(dto.getTargetId(), EntityStatus.ACTIVE)
                .stream().map(entity -> {
                    entity.setStatus(EntityStatus.CLOSED);
                    entity.setClosedAt(curTime);
                    entity.setCloseReason(dto.getCloseReason());
                    return entity;
                }).collect(Collectors.toList());
        PriceEntity newEntity = PriceConverter.convert(dto, new PriceEntity());

        if (!priceEntityList.isEmpty()) {
            PriceEntity latestPrice = priceEntityList.get(0);
            if (checkSamePrice(latestPrice, newEntity)) {
                throw new SamePriceException(String.format("The price is same with the latest price: %s %s",
                        latestPrice.getAmount(), latestPrice.getCurrency()));
            }
        }
        priceRepository.saveAll(priceEntityList);

        newEntity = priceRepository.save(newEntity);
        pushEvent(newEntity);
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

    private void pushEvent(PriceEntity entity) {
        AbstractPriceEvent event = new PriceCreatedEvent();
        makeEvent(event, entity);
        publisher.publishEvent(event);
    }

    private void makeEvent(AbstractPriceEvent event, PriceEntity entity) {
        event.setId(entity.getId());
        event.setCreatedAt(entity.getCreatedAt());
        event.setAmount(entity.getAmount());
        event.setTargetId(entity.getTargetId());
        event.setTargetType(entity.getTargetType());
        event.setStatus(entity.getStatus());
    }

    private boolean checkSamePrice(PriceEntity oldPrice, PriceEntity newPrice) {
        return oldPrice.getAmount() != null && oldPrice.getAmount().compareTo(newPrice.getAmount()) == 0
                && oldPrice.getCurrency() == newPrice.getCurrency();
    }

}
