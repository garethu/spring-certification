package com.example.springcertification.lab.scope;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class LabScope implements Scope {
    // Study note: scope storage is keyed by bean name and isolated to the current thread's conversation.
    private final ThreadLocal<Map<String, Object>> instances = ThreadLocal.withInitial(HashMap::new);
    private final ThreadLocal<Map<String, Runnable>> destructionCallbacks = ThreadLocal.withInitial(HashMap::new);

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        // Study note: create once per bean name in this conversation; subsequent lookups reuse it.
        return instances.get().computeIfAbsent(name, ignored -> objectFactory.getObject());
    }

    @Override
    public Object remove(String name) {
        destructionCallbacks.get().remove(name);
        return instances.get().remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        destructionCallbacks.get().put(name, callback);
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return "lab-" + Thread.currentThread().getName();
    }

    public void endConversation() {
        // Study note: custom scopes own their cleanup callbacks; Spring does not know when this lab conversation ends.
        destructionCallbacks.get().values().forEach(Runnable::run);
        destructionCallbacks.remove();
        instances.remove();
    }
}