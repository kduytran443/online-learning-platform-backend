package com.kduytran.userservice.event.application;

import com.kduytran.userservice.event.UserEvent;
import com.kduytran.userservice.event.UserRegisteredEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.kduytran.userservice.utils.LogUtils.withCorrelation;

@Component
@AllArgsConstructor
@Slf4j
@Primary
public class UserApplicationEventListener {
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    private final ModelMapper modelMapper;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserRegistered(UserRegisteredEvent event) {
        log.debug(withCorrelation(event.getCorrelationId(), "Sending user registration message"), event);
        UserEvent userEvent = modelMapper.map(event, UserEvent.class);

        ProducerRecord<String, UserEvent> record = new ProducerRecord<>("t_users", event.getId(), userEvent);
        record.headers().add("correlation-id", event.getCorrelationId().getBytes());
        kafkaTemplate.send(record);

        log.debug(withCorrelation(event.getCorrelationId(), "User registration event {}"), event);
    }

}
