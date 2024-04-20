package com.kduytran.notificationservice.notifier.email;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationEmailTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @Test
    public void testGetDefaultSubject() {
        RegistrationEmail registrationEmail = this.generateRegistrationEmail(null, null);
        Assertions.assertEquals("Online Learning Platform - Account Registration Confirmation",
                                registrationEmail.getDefaultSubject());
    }

    @Test
    public void testGetTemplate() {
        RegistrationEmail registrationEmail = this.generateRegistrationEmail(null, null);
        Assertions.assertEquals("registration-mail-template.html", registrationEmail.getTemplate());
    }

    @Test
    public void testGetRequiredAttributeList() {
        RegistrationEmail registrationEmail = this.generateRegistrationEmail(null, null);
        String[] expectedAttributes = {"username", "name", "email", "userType", "token", "expiredDate"};
        assertThat(registrationEmail.getRequiredAttributeList()).containsOnly(expectedAttributes);
    }

    @Test
    public void testPreCheck() {
        RegistrationEmail registrationEmail = this.generateRegistrationEmail(null, null);

        // Always return null because Registration Email class do not have preCheck
        assertThat(registrationEmail.preCheck()).isNull();
    }

    @Test
    public void testPostCheck() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("expiredDate", LocalDateTime.now().plusHours(1));
        RegistrationEmail registrationEmail = this.generateRegistrationEmail(variables, null);

        // Return null because the expiredDate has valid date
        assertThat(registrationEmail.preCheck()).isNull();
    }

    @Test
    public void testPostCheckWithError() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("expiredDate", LocalDateTime.now().minusHours(1));
        RegistrationEmail registrationEmail = this.generateRegistrationEmail(variables, null);

        assertThat(registrationEmail.postCheck())
                .containsExactly("Unable to send registration email because the expiration date has passed.");
    }

    @Test
    public void testSend() {
        String recipientEmail = "trankhanhduy18@gmail.com";
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", "duytran");
        variables.put("name", "Tran Khanh Duy");
        variables.put("email", "trankhanhduy18@gmail.com");
        variables.put("userType", "ADMIN");
        variables.put("token", UUID.randomUUID().toString());
        variables.put("expiredDate", LocalDateTime.now().plusHours(1));

        RegistrationEmail registrationEmail = RegistrationEmail
                                                .of(mailSender, templateEngine,variables, recipientEmail);

        Mockito.when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        Mockito.when(templateEngine.process(any(String.class), any(Context.class)))
                .thenReturn("src/main/resources/templates/email/registration-mail-template.html");

        try {
            assertThat(registrationEmail.send()).isEmpty();
        } catch (MessagingException e) {
            fail("Unexpected error is happened: " + e);
        }
    }

    @Test
    public void testSendWithEmailSenderNull() throws MessagingException {
        RegistrationEmail registrationEmail = RegistrationEmail
                                                .of(null, null, null, null);

        assertThat(registrationEmail.send())
                .contains("Email sender is not set.");
    }

    @Test
    public void testSendWithTemplateEngineNull() throws MessagingException {
        RegistrationEmail registrationEmail = RegistrationEmail
                                                .of(mailSender, null, null, null);

        assertThat(registrationEmail.send())
                .contains("Template engine is not set.");
    }

    @Test
    public void testSendWithRecipientEmailsNullOrEmpty() throws MessagingException {
        RegistrationEmail registrationEmailNull = RegistrationEmail
                                                    .of(mailSender, templateEngine, null, null);
        RegistrationEmail registrationEmailEmpty = RegistrationEmail.of(mailSender, templateEngine, null);

        assertThat(registrationEmailNull.send())
                .contains("Recipient email(s) is not set.");
        assertThat(registrationEmailEmpty.send())
                .contains("Recipient email(s) is not set.");
    }

    @Test
    public void testSendWithVariablesNull() throws MessagingException {
        RegistrationEmail registrationEmail = RegistrationEmail
                                                .of(mailSender, templateEngine, null, "Recipient");

        assertThat(registrationEmail.send())
                .contains("Variables is not set.");
    }

    @Test
    public void testSendWithMissingAttributes() throws MessagingException {
        RegistrationEmail registrationEmail = RegistrationEmail
                                                .of(mailSender, templateEngine, new HashMap<>(), "Recipient");

        List<String> expectedMissingAttributes = registrationEmail.getRequiredAttributeList().stream()
                .map(attr -> "Email is missing required attribute: " + attr)
                .collect(Collectors.toList());

        assertThat(registrationEmail.send())
                .containsAll(expectedMissingAttributes);
    }

    // Helper method to generate RegistrationEmail instance
    private RegistrationEmail generateRegistrationEmail(Map<String, Object> variables, String... recipientEmails) {
        return RegistrationEmail.of(mailSender, null, variables, recipientEmails);
    }

}
