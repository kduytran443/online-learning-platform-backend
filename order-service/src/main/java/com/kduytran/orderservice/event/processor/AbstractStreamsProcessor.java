package com.kduytran.orderservice.event.processor;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Component
public abstract class AbstractStreamsProcessor<T> {
    @Autowired
    private StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    public abstract String getStoreName();

    public ReadOnlyKeyValueStore<String, T> getStore() {
        KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        if (kafkaStreams == null) {
            return null;
        }
        return kafkaStreams.store(StoreQueryParameters.fromNameAndType(
                getStoreName(),
                QueryableStoreTypes.keyValueStore()
        ));
    }

    public List<T> findAllFromStore(ReadOnlyKeyValueStore<String, T> store) {
        if (store == null) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>();
        try(var keyValueIterator = store.all()) {
            while (keyValueIterator.hasNext()) {
                var pair = keyValueIterator.next();
                list.add(pair.value);
            }
        }
        return list;
    }

    public T findByKey(String key) {
        return getStore().get(key);
    }

    public List<T> findAllFromStore(ReadOnlyKeyValueStore<String, T> store,
                                    Predicate<T> predicate) {
        if (store == null) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>();
        try(var keyValueIterator = store.all()) {
            while (keyValueIterator.hasNext()) {
                var pair = keyValueIterator.next();
                if (predicate.test(pair.value)) {
                    list.add(pair.value);
                }
            }
        }
        return list;
    }

    @PostConstruct
    protected abstract void handleStream();

}
