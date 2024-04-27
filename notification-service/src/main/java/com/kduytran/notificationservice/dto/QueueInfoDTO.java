package com.kduytran.notificationservice.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("queue-list")
public class QueueInfoDTO {
    private String exchange;
    private QueueItemDTO userRegistered;
}
