package com.example.springcertification.day2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DayTwoDemo implements CommandLineRunner {
    private final ConstructorInjectionExample constructorExample;
    private final SetterInjectionExample setterExample;
    private final FieldInjectionExample fieldExample;
    private final NotificationService notificationService;

    public DayTwoDemo(
            ConstructorInjectionExample constructorExample,
            SetterInjectionExample setterExample,
            FieldInjectionExample fieldExample,
            NotificationService notificationService) {
        this.constructorExample = constructorExample;
        this.setterExample = setterExample;
        this.fieldExample = fieldExample;
        this.notificationService = notificationService;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=== Day 2: Dependency Injection Lab ===");
        System.out.println("Constructor: " + constructorExample.message());
        System.out.println("Setter: " + setterExample.message());
        System.out.println("Field: " + fieldExample.message());
        System.out.println("@Primary sender: " + notificationService.sendWithPrimary("hello"));
        System.out.println("@Qualifier sender: " + notificationService.sendWithSms("hello"));
        System.out.println("=== Day 2 demo complete ===\n");
    }
}