package com.geobudget.geobudget.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.application.url}")
    private String url;

    @Value("templates/confirm_register.html")
    private ClassPathResource confirmationTemplate;

    @Value("templates/reset_password_confirm.html")
    private ClassPathResource resetPasswordTemplate;

    @Value("templates/temp_password.html")
    private ClassPathResource tempPasswordTemplate;

    public void sendConfirmationEmail(String to, String token) throws MessagingException, IOException {
        String link = url + "/api/v1/users/confirm?token=" + token;

        String htmlContent;
        try (InputStream is = confirmationTemplate.getInputStream()) {
            htmlContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        htmlContent = htmlContent.replace("{{CONFIRMATION_LINK}}", link);

        sendEmail(to, "Подтверждение регистрации", htmlContent, "html");
    }

    public void sendPasswordResetConfirmationEmail(String to, String token) throws IOException, MessagingException {
        String link = url + "/api/v1/users/password/reset-confirm?token=" + token;

        String htmlContent;
        try (InputStream is = resetPasswordTemplate.getInputStream()) {
            htmlContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        htmlContent = htmlContent.replace("{{LINK}}", link);

        sendEmail(to, "Подтверждение сброса пароля", htmlContent, "html");
    }

    public void sendTemporaryPasswordEmail(String to, String tempPassword) throws IOException, MessagingException {
        String htmlContent;
        try (InputStream is = tempPasswordTemplate.getInputStream()) {
            htmlContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        htmlContent = htmlContent.replace("{{TEMP_PASSWORD}}", tempPassword);

        sendEmail(to, "Ваш временный пароль", htmlContent, "html");
    }

    private void sendEmail(String to, String subject, String body, String typeMessage) throws MessagingException {
       if (typeMessage.equals("html")) {
           MimeMessage message = mailSender.createMimeMessage();
           MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

           helper.setFrom(from);
           helper.setTo(to);
           helper.setSubject(subject);
           helper.setText(body, true);

           mailSender.send(message);
       } else {
           // TODO: Добавить отправку текстового сообщения
       }
    }
}
