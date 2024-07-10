package ru.cinimex.taskmanagerservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.cinimex.taskmanagerservice.domain.EmailMessage;

@RequiredArgsConstructor
@Service
public class EmailSenderService {

    private final JavaMailSender mailSender;

    public void sendConfirmationMessage(EmailMessage msg) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(msg.getTo());
        message.setSubject(msg.getSubject());
        message.setText(msg.getBody());
        mailSender.send(message);
    }

}
