package com.example.springcertification.day2;

import org.springframework.stereotype.Component;

@Component
public class ConstructorInjectionExample {
    private final ExampleDependency dependency;

    public ConstructorInjectionExample(ExampleDependency dependency) {
        this.dependency = dependency;
    }

    public String message() {
        return dependency.message();
    }
}