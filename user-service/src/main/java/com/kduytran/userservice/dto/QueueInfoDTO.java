package com.kduytran.userservice.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("queue-list")
public record QueueInfoDTO (
        String exchange,
        QueueItemDTO userRegistered
) {
}

@Data
class QueueItemDTO {
    private String queue;
    private String routingKey;
}