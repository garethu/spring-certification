package com.garethyoo.springcertification.day3;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class CatalogRepository {
    public List<String> findFeaturedItems() {
        return List.of("Spring Core", "Spring Boot");
    }
}