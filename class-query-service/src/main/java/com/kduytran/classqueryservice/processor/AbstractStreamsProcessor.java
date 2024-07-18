package com.kduytran.classqueryservice.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractStreamsProcessor<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractStreamsProcessor.class);
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

    @PostConstruct
    protected abstract void handleStream();

}
