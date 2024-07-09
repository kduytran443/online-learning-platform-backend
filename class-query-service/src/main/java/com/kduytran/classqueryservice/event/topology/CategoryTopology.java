package com.kduytran.classqueryservice.event.topology;

import com.kduytran.classqueryservice.constant.KafkaConstant;
import com.kduytran.classqueryservice.converter.CategoryConverter;
import com.kduytran.classqueryservice.dto.CategoryDTO;
import com.kduytran.classqueryservice.event.CategoryEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

@Component
public class CategoryTopology {

    @Autowired
    private StreamsBuilder streamsBuilder;

    @Autowired
    public void process() {
//        KStream<String, String> stringKStream = streamsBuilder
//                .stream("Test-Kafka", Consumed.with(Serdes.String(), Serdes.String()))
//                .peek((key, val) -> System.out.println(String.format("Successful - %s: %s", key, val)));

//        KStream<String, CategoryEvent> categoryKStream = streamsBuilder
//                .stream(KafkaConstant.TOPIC_CATEGORIES,
//                        Consumed.with(Serdes.String(), new JsonSerde<>(CategoryEvent.class)))
//                .peek((key, value) -> System.out.println(String.format("Successful - %s: %s", value.getCode(), value.getName())));
//        // type-safe method
//        categoryKStream.print(Printed.<String, CategoryEvent>toSysOut().withLabel("Category"));
//        KTable<String, CategoryDTO> categoryDTOKTable = categoryKStream
//                .map((key, value) -> KeyValue.pair(key, CategoryConverter.convert(value)))
//                .groupBy((key, value) -> key, Grouped.with(Serdes.String(), new JsonSerde<>(CategoryDTO.class)))
//                .reduce((aggValue, newValue) -> newValue,
//                        Materialized.<String, CategoryDTO, KeyValueStore<Bytes, byte[]>>as("category-store")
//                                .withKeySerde(Serdes.String())
//                                .withValueSerde(new JsonSerde<>(CategoryDTO.class))
//                );
    }

}
