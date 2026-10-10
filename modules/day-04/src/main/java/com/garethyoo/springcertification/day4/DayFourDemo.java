package com.garethyoo.springcertification.day4;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DayFourDemo implements CommandLineRunner {
    private final ReportOperations reportOperations;

    public DayFourDemo(ReportOperations reportOperations) {
        this.reportOperations = reportOperations;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=== Day 4: AOP Concepts Lab ===");
        System.out.println(reportOperations.generate("weekly-sales"));
        try {
            reportOperations.generate(" ");
        } catch (IllegalArgumentException exception) {
            System.out.println("Service exception reached caller: " + exception.getMessage());
        }
        System.out.println("=== Day 4 demo complete ===\n");
    }
}