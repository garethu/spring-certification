package com.garethyoo.springcertification.day3;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lab.catalog")
public record CatalogProperties(String edition) {
}