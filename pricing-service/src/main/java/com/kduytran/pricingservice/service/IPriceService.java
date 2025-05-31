package com.kduytran.pricingservice.service;

import com.kduytran.pricingservice.dto.CreatePriceDTO;
import com.kduytran.pricingservice.dto.PriceDTO;

import java.util.List;
import java.util.UUID;

public interface IPriceService {
    UUID createPrice(CreatePriceDTO dto);
    List<PriceDTO> getAllByTargetId(String targetId);
    PriceDTO getOneByTargetId(String targetId);
}
