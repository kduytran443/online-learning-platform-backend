package com.kduytran.classqueryservice.processor;

import com.kduytran.classqueryservice.constant.KafkaConstant;
import com.kduytran.classqueryservice.dto.CategoryDTO;
import com.kduytran.classqueryservice.event.CategoryEvent;
import com.kduytran.classqueryservice.utils.StreamUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.*;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryStreamProcessor {
    public static final String STORE_NAME = "category-store";
    private final StreamsBuilder streamsBuilder;
    private final ModelMapper modelMapper;
    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    public List<CategoryDTO> findAll() {
        ReadOnlyKeyValueStore<String, CategoryDTO> storeData = streamsBuilderFactoryBean.getKafkaStreams()
                .store(StoreQueryParameters.fromNameAndType(
                        STORE_NAME,
                        QueryableStoreTypes.keyValueStore()
                ));
        List<CategoryDTO> categories = new ArrayList<>();
        try (KeyValueIterator<String, CategoryDTO> all = storeData.all()) {
            while (all.hasNext()) {
                KeyValue<String, CategoryDTO> keyValue = all.next();
                categories.add(keyValue.value);
            }
        }
        return categories;
    }
    public CategoryDTO findOneById(String id) {
        ReadOnlyKeyValueStore<String, CategoryDTO> storeData = streamsBuilderFactoryBean.getKafkaStreams()
                .store(StoreQueryParameters.fromNameAndType(
                        STORE_NAME,
                        QueryableStoreTypes.keyValueStore()
                ));
        return storeData.get(id);
    }

    // Find category and all its child
    public List<CategoryDTO> findAllById(String categoryId) {
        var store = getStore();
        if (store == null) {
            return Collections.emptyList();
        }

        CategoryDTO rootCategory = store.get(categoryId);
        List<CategoryDTO> allCategories = findAll().stream()
                .filter(category -> categoryId.equals(category.getParentCategoryId()))
                .collect(Collectors.toList());
        allCategories.add(rootCategory);

        return allCategories;
    }

    private ReadOnlyKeyValueStore<String, CategoryDTO> getStore() {
        KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        if (kafkaStreams == null) {
            return null;
        }
        return kafkaStreams
                .store(StoreQueryParameters.fromNameAndType(
                        STORE_NAME,
                        QueryableStoreTypes.keyValueStore()
                ));
    }

    @PostConstruct
    private void stream() {
        KStream<String, CategoryEvent> categoryKStream = streamsBuilder.stream(KafkaConstant.TOPIC_CATEGORIES,
                        Consumed.with(Serdes.String(), Serdes.String()))
                .mapValues(value -> StreamUtils.mapValue(value, CategoryEvent.class))
                .peek((key, value)
                        -> System.out.println(String.format("Successful - %s: %s", value.getCode(), value.getName())));
        categoryKStream
                .mapValues(value -> modelMapper.map(value, CategoryDTO.class))
                .toTable(
                        Materialized.<String, CategoryDTO, KeyValueStore<Bytes, byte[]>>as(STORE_NAME)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new JsonSerde<>(CategoryDTO.class))
                );
    }

}
