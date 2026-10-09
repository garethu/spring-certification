package com.garethyoo.springcertification.lab.scope;

import java.util.UUID;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
// Study note: there is no @Prototype annotation. @Scope selects a scope by name.
// "lab" is a custom scope registered by LabConfiguration, not Spring's prototype scope.
@Scope("lab")
public class ConversationProbe implements DisposableBean {
    private final String id = UUID.randomUUID().toString();

    public ConversationProbe() {
        System.out.println("[constructor] ConversationProbe " + id);
    }

    public String id() {
        return id;
    }

    @Override
    public void destroy() {
        System.out.println("[lifecycle] ConversationProbe destroy " + id);
    }
}