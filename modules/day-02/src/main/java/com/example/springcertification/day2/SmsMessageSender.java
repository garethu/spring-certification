package com.example.springcertification.day2;

import org.springframework.stereotype.Component;

@Component("smsMessageSender")
public class SmsMessageSender implements MessageSender {
    @Override
    public String send(String message) {
        return "sms: " + message;
    }
}