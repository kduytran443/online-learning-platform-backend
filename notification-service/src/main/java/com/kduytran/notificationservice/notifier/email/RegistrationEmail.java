package com.kduytran.notificationservice.notifier.email;

import com.kduytran.notificationservice.constant.AttributeConstant;
import com.kduytran.notificationservice.utils.TimeUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RegistrationEmail extends AbstractEmail {

    private RegistrationEmail(JavaMailSender emailSender, SpringTemplateEngine templateEngine, String subject,
                             Map<String, Object> variables, String... recipientEmails) {
        super(emailSender, templateEngine, subject, variables, recipientEmails);
    }

    public static RegistrationEmail of(JavaMailSender emailSender, SpringTemplateEngine templateEngine, Map<String, Object> variables, String... recipientEmails) {
        return new RegistrationEmail(emailSender, templateEngine, null, variables, recipientEmails);
    }

    public static RegistrationEmail ofWithCustomSubject(JavaMailSender emailSender, SpringTemplateEngine templateEngine, String subject,
                                                        Map<String, Object> variables, String... recipientEmails) {
        return new RegistrationEmail(emailSender, templateEngine, subject, variables, recipientEmails);
    }

    /**
     * Retrieves the default subject for the email.
     *
     * @return the default subject for the email
     */
    @Override
    public String getDefaultSubject() {
        return "Online Learning Platform - Account Registration Confirmation";
    }

    /**
     * Retrieves the name of the email template to be used for sending emails.
     *
     * @return the name of the email template
     */
    @Override
    public String getTemplate() {
        return "registration-mail-template.html";
    }

    /**
     * Retrieves a list of required attributes for the email template.
     * These attributes will be used to validate the presence of necessary variables in the variables map.
     *
     * @return a list of required attributes
     */
    @Override
    public List<String> getRequiredAttributeList() {
        return Arrays.asList(
                AttributeConstant.USERNAME_ATTRIBUTE,
                AttributeConstant.NAME_ATTRIBUTE,
                AttributeConstant.EMAIL_ATTRIBUTE,
                AttributeConstant.USER_TYPE_ATTRIBUTE,
                AttributeConstant.TOKEN_ATTRIBUTE,
                AttributeConstant.EXPIRED_DATE_ATTRIBUTE
        );
    }

    /**
     * Abstract method for pre-processing before sending an email.
     * Subclasses should implement this to perform tasks such as data initialization
     * or validation. This method does not return a value and should not throw exceptions.
     */
    @Override
    public void preHandle() {}

    /**
     * Abstract method for post-processing after sending an email.
     * Subclasses should implement this method to perform tasks such as cleanup,
     * logging, or additional validation after the email has been sent.
     * <p>
     * This method does not return a value and should not throw exceptions.
     * Any errors or issues encountered should be handled within the implementation.
     */
    @Override
    public void postHandle() {
        String formattedDateTimeString = TimeUtils.getFormattedDate(
                                        this.getVariables().get(AttributeConstant.EXPIRED_DATE_ATTRIBUTE),
                                        TimeUtils.FULL_DATETIME_FORMAT);

        this.getVariables().put(AttributeConstant.EXPIRED_DATE_ATTRIBUTE, formattedDateTimeString);
    }

    /**
     * Performs pre-check operations specific to the concrete implementation.
     * Subclasses can override this method to customize pre-check behavior.
     *
     * @return a list of error messages encountered during pre-check, or an empty list if no errors occurred
     */
    @Override
    public List<String> preCheck() {
        Map<String, String> variables = this.getVariables().entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(),
                        e -> String.valueOf(e.getValue())));

        // If the expiration date has passed, return an error message
        // indicating that the registration email cannot be sent
        if (variables.containsKey(AttributeConstant.EXPIRED_DATE_ATTRIBUTE)) {
            LocalDateTime time = LocalDateTime.parse(variables.get(AttributeConstant.EXPIRED_DATE_ATTRIBUTE));
            if (LocalDateTime.now().isAfter(time)) {
                return Arrays.asList("Unable to send registration email because the expiration date has passed.");
            }
        }
        return null;
    }

    /**
     * Performs post-check operations specific to the concrete implementation.
     * Subclasses can override this method to customize post-check behavior.
     *
     * @return a list of error messages encountered during post-check, or an empty list if no errors occurred
     */
    @Override
    public List<String> postCheck() {
        return null;
    }

}
