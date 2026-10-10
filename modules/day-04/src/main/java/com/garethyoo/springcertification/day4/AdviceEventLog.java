package com.garethyoo.springcertification.day4;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AdviceEventLog {
    private final List<String> events = new ArrayList<>();

    public void record(String event) {
        events.add(event);
        System.out.println("[AOP] " + event);
    }

    public List<String> events() {
        return List.copyOf(events);
    }

    public void clear() {
        events.clear();
    }
}