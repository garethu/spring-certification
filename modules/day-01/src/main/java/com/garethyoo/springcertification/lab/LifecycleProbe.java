package com.garethyoo.springcertification.lab;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class LifecycleProbe {

    // Study note: Spring resolves constructor arguments first, then calls this constructor.
    public LifecycleProbe() {
        System.out.println("[constructor] LifecycleProbe");
    }

    // Study note: called after construction and dependency/property setup, during initialization.
    @PostConstruct
    void initialize() {
        System.out.println("[lifecycle] LifecycleProbe @PostConstruct");
    }

    // Study note: called when the ApplicationContext closes; prototype destruction is not automatic.
    @PreDestroy
    void destroy() {
        System.out.println("[lifecycle] LifecycleProbe @PreDestroy");
    }
}