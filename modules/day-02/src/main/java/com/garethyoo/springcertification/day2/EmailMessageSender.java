package com.garethyoo.springcertification.day2;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class EmailMessageSender implements MessageSender {
    @Override
    public String send(String message) {
        return "email: " + message;
    }
}