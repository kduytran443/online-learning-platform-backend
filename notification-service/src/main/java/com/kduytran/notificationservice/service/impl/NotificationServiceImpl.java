package com.kduytran.notificationservice.service.impl;

import com.kduytran.notificationservice.dto.RegistrationMessageDTO;
import com.kduytran.notificationservice.notifier.email.AbstractEmail;
import com.kduytran.notificationservice.notifier.email.RegistrationEmail;
import com.kduytran.notificationservice.service.INotificationService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationServiceImpl implements INotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public NotificationServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendRegistrationEmail(RegistrationMessageDTO messageDTO) {
        String recipientEmail = messageDTO.getEmail();
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", messageDTO.getUsername());
        variables.put("name", messageDTO.getName());
        variables.put("email", messageDTO.getEmail());
        variables.put("userType", messageDTO.getUserType());
        variables.put("token", messageDTO.getToken());
        variables.put("expiredDate", messageDTO.getExpiredDate());

        AbstractEmail email = RegistrationEmail.of(
                mailSender, templateEngine, variables, recipientEmail
        );

        try {
            List<String> errors = email.send();
            if (errors.isEmpty()) {
                LOGGER.info("Sent mail to {} successfully", email);
            } else {
                LOGGER.error("Sent mail to {} unsuccessfully", email);
            }
        } catch (MessagingException e) {
            LOGGER.error(e.toString());
        }

    }
}
