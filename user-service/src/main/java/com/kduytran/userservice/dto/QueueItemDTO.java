package com.kduytran.userservice.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class QueueItemDTO {
    private String queue;
    private String routingKey;
}