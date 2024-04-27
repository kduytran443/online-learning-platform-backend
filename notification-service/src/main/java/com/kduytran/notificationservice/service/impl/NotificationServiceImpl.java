package com.kduytran.notificationservice.service.impl;

import com.kduytran.notificationservice.constant.AttributeConstant;
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
        variables.put(AttributeConstant.USERNAME_ATTRIBUTE, messageDTO.getUsername());
        variables.put(AttributeConstant.NAME_ATTRIBUTE, messageDTO.getName());
        variables.put(AttributeConstant.EMAIL_ATTRIBUTE, messageDTO.getEmail());
        variables.put(AttributeConstant.USER_TYPE_ATTRIBUTE, messageDTO.getUserType());
        variables.put(AttributeConstant.TOKEN_ATTRIBUTE, messageDTO.getToken());
        variables.put(AttributeConstant.EXPIRED_DATE_ATTRIBUTE, messageDTO.getExpiredDate());
        variables.put(AttributeConstant.CREATED_AT_ATTRIBUTE, messageDTO.getCreatedAt());

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
