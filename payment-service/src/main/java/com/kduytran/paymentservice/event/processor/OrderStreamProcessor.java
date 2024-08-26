package com.kduytran.paymentservice.event.processor;

import com.kduytran.paymentservice.constant.KafkaConstant;
import com.kduytran.paymentservice.dto.PaymentRequestDTO;
import com.kduytran.paymentservice.event.order.OrderEvent;
import com.kduytran.paymentservice.service.IPaymentService;
import com.kduytran.paymentservice.util.StreamUtils;
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
public class OrderStreamProcessor extends AbstractStreamsProcessor {
    private final StreamsBuilder streamsBuilder;
    private final ModelMapper modelMapper;
    private final IPaymentService paymentService;

    @Override
    protected void handleStream() {
        KStream<String, OrderEvent> kStream = streamsBuilder.stream(KafkaConstant.TOPIC_ORDERS,
                Consumed.with(Serdes.String(), Serdes.String()))
                .mapValues(value -> StreamUtils.mapValue(value, OrderEvent.class));

        kStream.foreach((key, value) -> handleService(value));
    }

    private void handleService(OrderEvent event) {
        switch (event.getAction()) {
            case CREATED -> handleCreate(event);
        }
    }

    private void handleCreate(OrderEvent event) {
        log.info("Correlation ID: {} - Processing OrderEvent with action: {}, for Order ID: {}",
                event.getCorrelationId(),
                event.getAction(),
                event.getOrderId());
        PaymentRequestDTO dto = modelMapper.map(event, PaymentRequestDTO.class);
        paymentService.makeTransaction(dto);
    }

}
