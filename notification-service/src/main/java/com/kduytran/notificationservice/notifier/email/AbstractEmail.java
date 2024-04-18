package com.kduytran.notificationservice.notifier.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
public abstract class AbstractEmail {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEmail.class);
    private JavaMailSender emailSender;
    private SpringTemplateEngine templateEngine;
    private String subject;
    private String[] recipientEmails;
    private Map<String, Object> variables;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public AbstractEmail(JavaMailSender emailSender, SpringTemplateEngine templateEngine,
                         String subject, Map<String, Object> variables, String... recipientEmails) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.subject = subject;
        this.variables = variables;
        this.recipientEmails = recipientEmails;
    }

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

        if (subject == null || subject.isEmpty()) {
            errorList.add("Email subject is not set.");
        }

        if (senderEmail == null || senderEmail.isEmpty()) {
            errorList.add("Sender email is not set.");
        }

        if (recipientEmails == null || recipientEmails.length == 0) {
            errorList.add("Recipient email(s) is not set.");
        }

        if (getTemplate() == null || getTemplate().isEmpty()) {
            errorList.add("Email template is not set.");
        }

        // Variables map contains all the required attributes
        this.getRequiredAttributeList().stream()
                .filter(attr -> !variables.containsKey(attr))
                .forEach(attr -> errorList.add("Email is missing required attribute: " + attr));

        return errorList;
    }

    // Execute method
    public List<String> send() throws MessagingException {
        List<String> errorList = new ArrayList<>();
        errorList.addAll(preCheck());
        errorList.addAll(mainCheck());
        errorList.addAll(postCheck());

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
        helper.setText(html);

        LOGGER.info("Sending email with subject {}", this.subject);
        emailSender.send(message);

        return errorList;
    }

}
