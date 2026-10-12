package com.garethyoo.springcertification.day5;

import org.springframework.stereotype.Service;

@Service
public class OrderService {
    public String placeOrder(String item) {
        return "Order placed: " + item;
    }
}