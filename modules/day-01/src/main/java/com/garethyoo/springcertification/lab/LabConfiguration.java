package com.garethyoo.springcertification.lab;

import com.garethyoo.springcertification.lab.scope.LabScope;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LabConfiguration {

    @Bean
    static BeanFactoryPostProcessor registerLabScope() {
        return beanFactory -> {
            // Study note: register the custom scope name before Spring creates beans annotated @Scope("lab").
            System.out.println("[factory-post-processor] Registering custom 'lab' scope");
            LabScope labScope = new LabScope();
            beanFactory.registerScope("lab", labScope);
            beanFactory.registerSingleton("labScope", labScope);
        };
    }
}