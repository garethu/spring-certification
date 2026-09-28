package com.example.springcertification.lab;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
public class PrototypeConsumer {
    // Study note: this singleton's constructor receives one prototype; method calls do not reinject it.
    private final PrototypeProbe injectedOnce;

    // Study note: ObjectProvider delays lookup until getObject(), so each call obtains a fresh prototype.
    private final ObjectProvider<PrototypeProbe> provider;

    public PrototypeConsumer(PrototypeProbe injectedOnce, ObjectProvider<PrototypeProbe> provider) {
        this.injectedOnce = injectedOnce;
        this.provider = provider;
    }

    public String injectedPrototypeId() {
        return injectedOnce.id();
    }

    public String nextPrototypeId() {
        return provider.getObject().id();
    }
}