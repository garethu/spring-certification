package com.garethyoo.springcertification.day3;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class ConfigurationAndComponentsTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private CatalogProperties catalogProperties;

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private CatalogLabelFormatter catalogLabelFormatter;

    @Test
    void componentScanningRegistersStereotypeBeans() {
        assertThat(applicationContext.getBean(CatalogService.class)).isSameAs(catalogService);
        assertThat(applicationContext.getBean(CatalogRepository.class)).isSameAs(catalogRepository);
        assertThat(catalogService.featuredItems()).containsExactly("Spring Core", "Spring Boot");
    }

    @Test
    void configurationRegistersBeanAndBothPropertyInjectionStylesWork() {
        assertThat(applicationContext.getBean(CatalogLabelFormatter.class)).isSameAs(catalogLabelFormatter);
        assertThat(catalogProperties.edition()).isEqualTo("Day 3: Configuration and Components");
        assertThat(catalogService.catalogName()).isEqualTo("Certification Study Catalog");
        assertThat(catalogService.catalogEdition()).isEqualTo("Day 3: Configuration and Components");
        assertThat(catalogLabelFormatter.format(catalogService.catalogName()))
                .isEqualTo("[CERTIFICATION STUDY CATALOG]");
    }
}