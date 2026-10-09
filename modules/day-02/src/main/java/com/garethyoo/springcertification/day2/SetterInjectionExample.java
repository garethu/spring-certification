package com.garethyoo.springcertification.day2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetterInjectionExample {
    private ExampleDependency dependency;

    @Autowired
    public void setDependency(ExampleDependency dependency) {
        this.dependency = dependency;
    }

    public String message() {
        return dependency.message();
    }
}