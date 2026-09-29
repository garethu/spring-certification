package com.example.springcertification.day2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FieldInjectionExample {
    @Autowired
    private ExampleDependency dependency;

    public String message() {
        return dependency.message();
    }
}