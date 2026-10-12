package com.garethyoo.springcertification.day5;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public String send(String message) {
        return "Sent: " + message;
    }

    public String sendTo(long customerId) {
        return "Sent to customer " + customerId;
    }
}