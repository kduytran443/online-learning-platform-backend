package com.kduytran.notificationservice.event.handler;

import com.kduytran.notificationservice.constant.KafkaConstant;
import com.kduytran.notificationservice.dto.PaymentDTO;
import com.kduytran.notificationservice.dto.RegistrationMessageDTO;
import com.kduytran.notificationservice.event.UserEvent;
import com.kduytran.notificationservice.event.payment.PaymentEvent;
import com.kduytran.notificationservice.service.INotificationService;
import com.kduytran.notificationservice.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@Service
public class NotificationEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationEventHandler.class);
    private final INotificationService notificationService;
    private final ModelMapper modelMapper;

    public NotificationEventHandler(INotificationService notificationService, ModelMapper modelMapper) {
        this.notificationService = notificationService;
        this.modelMapper = modelMapper;
    }

    @KafkaListener(
            topics = "t_users",
            groupId = "notification-consumers",
            properties = {
                    "spring.json.value.default.type: com.kduytran.notificationservice.event.UserEvent"
            },
            containerFactory = "kafkaListener"
    )
    public void handle(UserEvent userEvent) {
        if (!"CREATE".equals(userEvent.getAction())) {
            return;
        }
        LOGGER.info(LogUtils.getLogDebugFormat("- Handling notification"), userEvent.getTransactionId());
        this.notificationService.sendRegistrationEmail(
                this.modelMapper.map(userEvent, RegistrationMessageDTO.class)
        );
    }

    @KafkaListener(
            topics = KafkaConstant.TOPIC_PAYMENTS,
            groupId = "notification-consumers",
            properties = {
                "spring.json.value.default.type: com.kduytran.notificationservice.event.payment.PaymentEvent"
            },
            containerFactory = "kafkaListener"
    )
    public void handlePaymentEvent(PaymentEvent event) {
        log.info("Correlation ID: {} - Receive PaymentEvent for order ID: {}",
                event.getCorrelationId(), event.getOrderId());
        if (event == null && event.getAction() == null) {
            return;
        }
        PaymentDTO paymentDTO = modelMapper.map(event, PaymentDTO.class);
        switch (event.getAction()) {
            case EXECUTE -> notificationService.sendPaymentEmail(paymentDTO);
        }
    }

}
