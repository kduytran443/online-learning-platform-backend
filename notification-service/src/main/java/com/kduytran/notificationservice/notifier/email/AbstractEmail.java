package com.kduytran.notificationservice.notifier.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This abstract class represents an email notifier used for sending emails.
 * Concrete implementations must extend this class to provide specific details such as template name and required attributes.
 *
 * <p>
 * This class follows the <strong>Template Method design pattern</strong>, where common steps for sending emails are defined in this class,
 * and specific steps are implemented in subclasses.
 * </p>
 */
@Getter
@Setter
public abstract class AbstractEmail {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEmail.class);
    private JavaMailSender emailSender;
    private SpringTemplateEngine templateEngine;
    private String subject;
    private String[] recipientEmails;
    private Map<String, Object> variables;

    private String senderEmail = "ctuinternshipdemo@gmail.com"; // TODO - refactor it instead of hard code

    public AbstractEmail(JavaMailSender emailSender, SpringTemplateEngine templateEngine,
                         String subject, Map<String, Object> variables, String... recipientEmails) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.subject = subject;
        this.variables = variables;
        this.recipientEmails = recipientEmails;
    }

    /**
     * Abstract method for pre-processing before sending an email.
     * Subclasses should implement this to perform tasks such as data initialization
     * or validation. This method does not return a value and should not throw exceptions.
     */
    public abstract void preHandle();

    /**
     * Abstract method for post-processing after sending an email.
     * Subclasses should implement this method to perform tasks such as cleanup,
     * logging, or additional validation after the email has been sent.
     *
     * This method does not return a value and should not throw exceptions.
     * Any errors or issues encountered should be handled within the implementation.
     */
    public abstract void postHandle();

    /**
     * Retrieves the default subject for the email.
     *
     * @return the default subject for the email
     */
    public abstract String getDefaultSubject();

    /**
     * Retrieves the name of the email template to be used for sending emails.
     *
     * @return the name of the email template
     */
    public abstract String getTemplate();

    /**
     * Retrieves a list of required attributes for the email template.
     * These attributes will be used to validate the presence of necessary variables in the variables map.
     *
     * @return a list of required attributes
     */
    public abstract List<String> getRequiredAttributeList();

    /**
     * Performs pre-check operations specific to the concrete implementation.
     * Subclasses can override this method to customize pre-check behavior.
     *
     * @return a list of error messages encountered during pre-check, or an empty list if no errors occurred
     */
    public abstract List<String> preCheck();

    /**
     * Performs post-check operations specific to the concrete implementation.
     * Subclasses can override this method to customize post-check behavior.
     *
     * @return a list of error messages encountered during post-check, or an empty list if no errors occurred
     */
    public abstract List<String> postCheck();

    public List<String> mainCheck() {
        List<String> errorList = new ArrayList<>();

        if (emailSender == null) {
            errorList.add("Email sender is not set.");
        }

        if (templateEngine == null) {
            errorList.add("Template engine is not set.");
        }

        if (recipientEmails == null || recipientEmails.length == 0) {
            errorList.add("Recipient email(s) is not set.");
        }

        if (getTemplate() == null || getTemplate().isEmpty()) {
            errorList.add("Email template is not set.");
        }

        if (this.getRequiredAttributeList() != null && !this.getRequiredAttributeList().isEmpty()) {
            if (variables == null) {
                errorList.add("Variables is not set.");
            } else {
                // Variables map contains all the required attributes
                this.getRequiredAttributeList().stream()
                        .filter(attr -> !variables.containsKey(attr) || variables.get(attr) == null)
                        .forEach(attr -> errorList.add("Email is missing required attribute: " + attr));
            }
        }

        return errorList;
    }

    // Execute method
    public List<String> send() throws MessagingException {
        this.preHandle();
        List<String> errorList = this.preCheck();
        if (errorList != null && !errorList.isEmpty()) {
            return errorList;
        }

        this.postHandle();
        errorList = this.mainCheck();
        if (errorList != null && !errorList.isEmpty()) {
            return errorList;
        }

        errorList = this.postCheck();
        if (errorList != null && !errorList.isEmpty()) {
            return errorList;
        }

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        helper.setSubject(this.subject == null ? this.getDefaultSubject() : this.subject);
        helper.setFrom(this.senderEmail);
        helper.setTo(this.recipientEmails);
        Context context = new Context();
        context.setVariables(variables);
        String html = templateEngine.process(getTemplate(), context);
        helper.setText(html, true);

        LOGGER.info("Sending email with subject: {}", this.subject == null ? this.getDefaultSubject() : this.subject);
        emailSender.send(message);

        if (errorList == null) {
            return Collections.emptyList();
        }

        return errorList;
    }

}
