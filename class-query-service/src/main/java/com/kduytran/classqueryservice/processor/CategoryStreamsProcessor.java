package com.kduytran.classqueryservice.processor;

import com.kduytran.classqueryservice.constant.KafkaConstant;
import com.kduytran.classqueryservice.dto.CategoryDTO;
import com.kduytran.classqueryservice.event.CategoryEvent;
import com.kduytran.classqueryservice.utils.LogUtils;
import com.kduytran.classqueryservice.utils.StreamUtils;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CategoryStreamsProcessor extends AbstractStreamsProcessor<CategoryDTO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryStreamsProcessor.class);
    private final StreamsBuilder streamsBuilder;
    private final ModelMapper modelMapper;

    public List<CategoryDTO> findAll() {
        return StreamUtils.findAllFromStore(getStore());
    }

    public CategoryDTO findOneById(String id) {
        var store = getStore();
        if (store == null) {
            return null;
        }
        return store.get(id);
    }

    @Override
    public String getStoreName() {
        return "category-store";
    }

    @Override
    protected void handleStream() {
        KStream<String, CategoryEvent> categoryKStream = streamsBuilder.stream(KafkaConstant.TOPIC_CATEGORIES,
                        Consumed.with(Serdes.String(), Serdes.String()))
                .mapValues(value -> StreamUtils.mapValue(value, CategoryEvent.class))
                .peek((key, value) -> LogUtils.logEvent(LOGGER, value));
        categoryKStream
                .mapValues(value -> modelMapper.map(value, CategoryDTO.class))
                .toTable(
                        Materialized.<String, CategoryDTO, KeyValueStore<Bytes, byte[]>>as(getStoreName())
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new JsonSerde<>(CategoryDTO.class))
                );
    }

}
