package com.garethyoo.springcertification.day3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogConfiguration {
    @Bean
    public CatalogLabelFormatter catalogLabelFormatter() {
        return new CatalogLabelFormatter();
    }
}