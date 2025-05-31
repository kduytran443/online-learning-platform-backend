package com.kduytran.classservice.event.application.listener;

import com.kduytran.classservice.event.application.AbstractClassApplicationEvent;
import com.kduytran.classservice.event.application.ClassCreatedApplicationEvent;
import com.kduytran.classservice.event.application.ClassDeletedApplicationEvent;
import com.kduytran.classservice.event.application.ClassUpdatedApplicationEvent;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
public class ClassApplicationEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassApplicationEventListener.class);
    private final KafkaTemplate<String, ClassCreatedApplicationEvent> createdKafkaTemplate;
    private final KafkaTemplate<String, ClassUpdatedApplicationEvent> updatedKafkaTemplate;
    private final KafkaTemplate<String, ClassDeletedApplicationEvent> deletedKafkaTemplate;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onClassCreated(ClassCreatedApplicationEvent event) {
        createdKafkaTemplate.send("t_classes", event.getId().toString(), event);
        log(event);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onClassUpdated(ClassUpdatedApplicationEvent event) {
        updatedKafkaTemplate.send("t_classes", event.getId().toString(), event);
        log(event);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onClassUpdated(ClassDeletedApplicationEvent event) {
        deletedKafkaTemplate.send("t_classes", event.getId().toString(), event);
        log(event);
    }

    private void log(AbstractClassApplicationEvent event) {
        LOGGER.info(String.format("Pushed %s: Action = %s, Id = %s",
                event.getClass().getName(),
                event.getAction(),
                event.getId()));
    }

}
