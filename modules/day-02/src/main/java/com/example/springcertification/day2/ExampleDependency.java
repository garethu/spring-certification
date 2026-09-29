package com.example.springcertification.day2;

import org.springframework.stereotype.Component;

@Component
public class ExampleDependency {
    public String message() {
        return "The dependency was injected.";
    }
}