package com.rakhimov.homework.service;

import com.rakhimov.homework.config.EmailConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final JavaMailSender mailSender;
    private final EmailConfig emailConfig;

    public void sendEmail(String subject, String text) {
        String recipientEmail = emailConfig.getRecipientEmail();
        if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Email адрес для отправки письма не может быть пустым. Заполните email.to в application.yaml");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setFrom("formyw@rambler.ru");
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}
