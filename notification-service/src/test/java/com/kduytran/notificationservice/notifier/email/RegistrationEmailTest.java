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
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class RegistrationEmailTest {
    @Mock
    private JavaMailSender mailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @Test
    public void testGetDefaultSubject() {
        RegistrationEmail registrationEmail = this.generateRegistrationEmail(null, null);
        Assertions.assertEquals("Online Learning Platform - Account Registration Confirmation", registrationEmail.getDefaultSubject());
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

    // Helper method to generate RegistrationEmail instance
    private RegistrationEmail generateRegistrationEmail(Map<String, Object> variables, String... recipientEmails) {
        return RegistrationEmail.of(mailSender, templateEngine, variables, recipientEmails);
    }

}
