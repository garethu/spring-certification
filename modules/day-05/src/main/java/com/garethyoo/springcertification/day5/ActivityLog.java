package com.garethyoo.springcertification.day5;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ActivityLog {
    private final List<String> entries = new ArrayList<>();

    public void record(String entry) {
        entries.add(entry);
        System.out.println("[POINTCUT] " + entry);
    }

    public List<String> entries() {
        return List.copyOf(entries);
    }

    public void clear() {
        entries.clear();
    }
}