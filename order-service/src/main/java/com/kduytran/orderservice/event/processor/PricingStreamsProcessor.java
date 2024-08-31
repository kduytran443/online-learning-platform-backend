package com.kduytran.orderservice.event.processor;

import com.kduytran.orderservice.constant.KafkaConstant;
import com.kduytran.orderservice.event.pricing.PriceDTO;
import com.kduytran.orderservice.event.pricing.PriceEvent;
import com.kduytran.orderservice.utils.MapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
@Slf4j
public class PricingStreamsProcessor extends AbstractStreamsProcessor<PriceDTO> {
    private final StreamsBuilder streamsBuilder;
    private final ModelMapper modelMapper;

    @Override
    public String getStoreName() {
        return "pricing-store";
    }

    @Override
    protected void handleStream() {
        streamsBuilder.stream(KafkaConstant.TOPIC_PRICES,
                Consumed.with(Serdes.String(), Serdes.String()))
                .mapValues(value -> MapUtils.mapValue(value, PriceEvent.class))
                .mapValues(value -> modelMapper.map(value, PriceDTO.class))
                .peek((key, value) -> log.info("Event received " + value))
                .toTable(
                        Materialized.<String, PriceDTO, KeyValueStore<Bytes, byte[]>>as(getStoreName())
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new JsonSerde<>(PriceDTO.class))
                );
    }

    public List<PriceDTO> getPriceListOfTarget(String targetId) {
        Predicate<PriceDTO> targetIdPredicate = priceDTO -> targetId.equals(priceDTO.getTargetId());
        List<PriceDTO> list = findAllFromStore(getStore(), targetIdPredicate);
        return list;
    }

}
