package com.kduytran.notificationservice.service;

import com.kduytran.notificationservice.dto.RegistrationMessageDTO;

public interface INotificationService {

    void sendRegistrationEmail(RegistrationMessageDTO messageDTO);

}
