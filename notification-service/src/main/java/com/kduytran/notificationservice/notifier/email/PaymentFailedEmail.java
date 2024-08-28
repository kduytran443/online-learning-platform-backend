package com.kduytran.notificationservice.notifier.email;

import com.kduytran.notificationservice.constant.AttributeConstant;
import com.kduytran.notificationservice.utils.TimeUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PaymentFailedEmail extends AbstractEmail {
    public PaymentFailedEmail(JavaMailSender emailSender, SpringTemplateEngine templateEngine, String subject,
                              Map<String, Object> variables, String... recipientEmails) {
        super(emailSender, templateEngine, subject, variables, recipientEmails);
    }

    @Override
    public void preHandle() {

    }

    @Override
    public void postHandle() {
        this.getVariables().put(AttributeConstant.EXECUTION_AT_ATTRIBUTE, TimeUtils.getFormattedDate(
                this.getVariables().get(AttributeConstant.EXECUTION_AT_ATTRIBUTE),
                TimeUtils.FULL_DATETIME_FORMAT));
    }

    @Override
    public String getDefaultSubject() {
        return "Online Learning Platform - Payment Success";
    }

    @Override
    public String getTemplate() {
        return "payment/success.html";
    }

    @Override
    public List<String> getRequiredAttributeList() {
        return Arrays.asList(
                AttributeConstant.FULL_NAME_ATTRIBUTE,
                AttributeConstant.ORDER_ID_ATTRIBUTE,
                AttributeConstant.PAYMENT_ID_ATTRIBUTE,
                AttributeConstant.TOTAL_ATTRIBUTE,
                AttributeConstant.CURRENCY_ATTRIBUTE,
                AttributeConstant.PAYMENT_METHOD_ATTRIBUTE,
                AttributeConstant.DESCRIPTION_ATTRIBUTE,
                AttributeConstant.EXECUTION_AT_ATTRIBUTE
        );
    }

    @Override
    public List<String> preCheck() {
        return null;
    }

    @Override
    public List<String> postCheck() {
        return null;
    }
}
