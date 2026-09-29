package com.example.springcertification.day2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class NotificationService {
    private final MessageSender primarySender;
    private final MessageSender smsSender;

    @Autowired
    public NotificationService(
            MessageSender primarySender,
            @Qualifier("smsMessageSender") MessageSender smsSender) {
        this.primarySender = primarySender;
        this.smsSender = smsSender;
    }

    public String sendWithPrimary(String message) {
        return primarySender.send(message);
    }

    public String sendWithSms(String message) {
        return smsSender.send(message);
    }
}