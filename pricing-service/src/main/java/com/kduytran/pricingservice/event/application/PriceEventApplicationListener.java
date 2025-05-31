package com.kduytran.pricingservice.event.application;

import com.kduytran.pricingservice.constant.KafkaConstant;
import com.kduytran.pricingservice.event.AbstractPriceEvent;
import com.kduytran.pricingservice.event.PriceCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
@Setter
@Getter
@Slf4j
public class PriceEventApplicationListener {
    private KafkaTemplate<String, PriceCreatedEvent> priceCreatedEventKafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePriceCreatedEvent(PriceCreatedEvent event) {
        log(event);
        priceCreatedEventKafkaTemplate.send(KafkaConstant.TOPIC_PRICES, event.getTargetId().toString(), event);
    }

    private void log(AbstractPriceEvent event) {
        log.info(String.format("Pushed %s: Action = %s, Id = %s",
                event.getClass().getName(),
                event.getEventType(),
                event.getId()));
    }

}
