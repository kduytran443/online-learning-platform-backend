package com.kduytran.paymentservice.processor;

import com.kduytran.paymentservice.constant.KafkaConstant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class PaymentProcessor extends AbstractStreamsProcessor {
    private final StreamsBuilder streamsBuilder;
    private final ModelMapper modelMapper;

    @Override
    protected void handleStream() {
        KStream<String, String> kStream = streamsBuilder.stream(KafkaConstant.TOPIC_ORDERS,
                Consumed.with(Serdes.String(), Serdes.String()));

    }

}
