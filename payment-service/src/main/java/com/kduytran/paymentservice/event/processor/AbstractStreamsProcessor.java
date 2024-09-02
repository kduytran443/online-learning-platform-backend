package com.kduytran.paymentservice.event.processor;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableKafka
@EnableKafkaStreams
public abstract class AbstractStreamsProcessor {

    @PostConstruct
    protected abstract void handleStream();

}
