package com.garethyoo.springcertification.day3;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CatalogService {
    private final CatalogRepository catalogRepository;
    private final String catalogName;
    private final CatalogProperties catalogProperties;

    public CatalogService(
            CatalogRepository catalogRepository,
            CatalogProperties catalogProperties,
            @Value("${lab.catalog.name}") String catalogName) {
        this.catalogRepository = catalogRepository;
        this.catalogProperties = catalogProperties;
        this.catalogName = catalogName;
    }

    public String describeCatalog() {
        return catalogName + " contains: " + String.join(", ", catalogRepository.findFeaturedItems());
    }

    public List<String> featuredItems() {
        return catalogRepository.findFeaturedItems();
    }

    public String catalogName() {
        return catalogName;
    }

    public String catalogEdition() {
        return catalogProperties.edition();
    }
}