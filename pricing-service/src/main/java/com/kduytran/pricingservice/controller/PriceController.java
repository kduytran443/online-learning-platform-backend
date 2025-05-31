package com.kduytran.pricingservice.controller;

import com.kduytran.pricingservice.constant.ResponseConstant;
import com.kduytran.pricingservice.dto.CreatePriceDTO;
import com.kduytran.pricingservice.dto.IdResponseDTO;
import com.kduytran.pricingservice.dto.PriceDTO;
import com.kduytran.pricingservice.service.IPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
@RestController
public class PriceController {
    private final IPriceService priceService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
        public IdResponseDTO createPrice(@RequestBody CreatePriceDTO dto) {
        return IdResponseDTO.of(ResponseConstant.STATUS_200,
                ResponseConstant.MESSAGE_200,
                priceService.createPrice(dto));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PriceDTO> getPriceListOfTarget(@RequestParam String targetId) {
        return priceService.getAllByTargetId(targetId);
    }

    @GetMapping("/latest-one")
    @ResponseStatus(HttpStatus.OK)
    public PriceDTO getLatestPriceOfTarget(@RequestParam String targetId) {
        return priceService.getOneByTargetId(targetId);
    }

}
