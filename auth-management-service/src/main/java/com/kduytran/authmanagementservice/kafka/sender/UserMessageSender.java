package com.kduytran.authmanagementservice.kafka.sender;

import com.kduytran.authmanagementservice.event.UserRegisteredEvent;
import com.kduytran.authmanagementservice.kafka.message.EventType;
import com.kduytran.authmanagementservice.mapper.MessageEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserMessageSender {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final MessageEventMapper messageEventMapper;
    @Value("${olp.dummy-topic}")
    private String topic;

    public void sendMessage(UserRegisteredEvent event) {
        var message = messageEventMapper.map(event.getSource());
        var key = message.getUsername();
        message.setType(EventType.CREATED);
        kafkaTemplate.send(topic, key, message);
    }
}
