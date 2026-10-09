package com.garethyoo.springcertification.day3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DayThreeDemo implements CommandLineRunner {
    private final CatalogService catalogService;
    private final CatalogLabelFormatter labelFormatter;

    public DayThreeDemo(CatalogService catalogService, CatalogLabelFormatter labelFormatter) {
        this.catalogService = catalogService;
        this.labelFormatter = labelFormatter;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=== Day 3: Configuration and Components Lab ===");
        System.out.println("Scanned @Service + @Repository: " + catalogService.describeCatalog());
        System.out.println("Property via @Value: " + catalogService.catalogName());
        System.out.println("Property via @ConfigurationProperties: " + catalogService.catalogEdition());
        System.out.println("Explicit @Bean: " + labelFormatter.format(catalogService.catalogName()));
        System.out.println("=== Day 3 demo complete ===\n");
    }
}