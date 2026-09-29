package com.example.springcertification.lab;

import java.util.UUID;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
// Study note: Spring has no @Prototype annotation; use @Scope with its built-in prototype value.
// Each container lookup creates a new instance. This differs from ConversationProbe's custom "lab" scope.
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrototypeProbe {
    private final String id = UUID.randomUUID().toString();

    public PrototypeProbe() {
        System.out.println("[constructor] PrototypeProbe " + id);
    }

    public String id() {
        return id;
    }
}