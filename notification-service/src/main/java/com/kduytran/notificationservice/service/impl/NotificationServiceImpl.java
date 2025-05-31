package com.kduytran.notificationservice.service.impl;

import com.kduytran.notificationservice.constant.AttributeConstant;
import com.kduytran.notificationservice.dto.PaymentDTO;
import com.kduytran.notificationservice.dto.RegistrationMessageDTO;
import com.kduytran.notificationservice.notifier.email.AbstractEmail;
import com.kduytran.notificationservice.notifier.email.PaymentSuccessEmail;
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
        sendMail(email);
    }

    @Override
    public void sendPaymentEmail(PaymentDTO paymentDTO) {
        String recipientEmail = paymentDTO.getEmail();
        Map<String, Object> variables = new HashMap<>();
        variables.put(AttributeConstant.FULL_NAME_ATTRIBUTE, paymentDTO.getFullName());
        variables.put(AttributeConstant.ORDER_ID_ATTRIBUTE, paymentDTO.getOrderId());
        variables.put(AttributeConstant.PAYMENT_ID_ATTRIBUTE, paymentDTO.getPaymentId());
        variables.put(AttributeConstant.TOTAL_ATTRIBUTE, paymentDTO.getTotal());
        variables.put(AttributeConstant.CURRENCY_ATTRIBUTE, paymentDTO.getCurrency());
        variables.put(AttributeConstant.PAYMENT_METHOD_ATTRIBUTE, paymentDTO.getPaymentMethod());
        variables.put(AttributeConstant.DESCRIPTION_ATTRIBUTE, paymentDTO.getDescription());
        variables.put(AttributeConstant.EXECUTION_AT_ATTRIBUTE, paymentDTO.getExecutionAt());
        variables.put(AttributeConstant.USERNAME_ATTRIBUTE, paymentDTO.getUsername());
        variables.put(AttributeConstant.EMAIL_ATTRIBUTE, paymentDTO.getEmail());
        variables.put(AttributeConstant.ORDER_DETAILS_LIST_ATTRIBUTE, paymentDTO.getOrderDetailsList());

        AbstractEmail email = new PaymentSuccessEmail(mailSender, templateEngine, null, variables, recipientEmail);
        sendMail(email);
    }

    private void sendMail(AbstractEmail email) {
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
