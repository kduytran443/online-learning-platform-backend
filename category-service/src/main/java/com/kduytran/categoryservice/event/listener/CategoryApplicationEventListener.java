package com.kduytran.categoryservice.event.listener;

import com.kduytran.categoryservice.constant.KafkaConstant;
import com.kduytran.categoryservice.event.CategoryCreatedEvent;
import com.kduytran.categoryservice.event.CategoryDeletedEvent;
import com.kduytran.categoryservice.event.CategoryEvent;
import com.kduytran.categoryservice.event.CategoryUpdatedEvent;
import com.kduytran.categoryservice.utils.LogUtils;
import com.kduytran.categoryservice.utils.TransactionIdHolder;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
public class CategoryApplicationEventListener {
    private static final Logger logger = LoggerFactory.getLogger(CategoryApplicationEventListener.class);
    private final KafkaTemplate<String, CategoryEvent> kafkaTemplate;
    private final TransactionIdHolder transactionIdHolder;
    private final ModelMapper modelMapper;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCategoryCreated(CategoryCreatedEvent event) {
        CategoryEvent categoryEvent = setupEvent(modelMapper.map(event, CategoryEvent.class), "CREATE");

        kafkaTemplate.send(KafkaConstant.KAFKA_T_CATEGORIES, categoryEvent.getId(), categoryEvent);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCategoryUpdated(CategoryUpdatedEvent event) {
        CategoryEvent categoryEvent = setupEvent(modelMapper.map(event, CategoryEvent.class), "UPDATE");

        kafkaTemplate.send(KafkaConstant.KAFKA_T_CATEGORIES, categoryEvent.getId(), categoryEvent);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCategoryUpdated(CategoryDeletedEvent event) {
        CategoryEvent categoryEvent = setupEvent(modelMapper.map(event, CategoryEvent.class), "DELETE");

        kafkaTemplate.send(KafkaConstant.KAFKA_T_CATEGORIES, categoryEvent.getId(), categoryEvent);
    }

    private CategoryEvent setupEvent(CategoryEvent event, String action) {
        String transactionId = transactionIdHolder.getCurrentTransactionId();
        logger.debug(LogUtils.getLogDebugFormat("Sending category event"), transactionId);
        event.setAction(action);
        event.setTransactionId(transactionId);
        return event;
    }

}
