package ru.cinimex.taskmanagerservice.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import ru.cinimex.taskmanagerservice.domain.EmailMessage;


@Service
@RequiredArgsConstructor
public class ProducerVerificationService {

    private final EmailSenderService emailSenderService;


    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "email-topic";

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;

    public void sendUserMessage(EmailMessage msg) {
        LOGGER.info(String.format("\n ===== Producing message in JSON ===== \n"+msg));
        Message<EmailMessage> message = MessageBuilder
                .withPayload(msg)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .build();
        this.kafkaTemplate.send(message);

        emailSenderService.sendConfirmationMessage(msg);
    }

}
