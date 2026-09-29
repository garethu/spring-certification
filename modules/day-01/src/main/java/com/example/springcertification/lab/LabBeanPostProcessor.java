package com.example.springcertification.lab;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class LabBeanPostProcessor implements BeanPostProcessor {

    // Study note: this hook receives bean instances before initialization callbacks such as @PostConstruct.
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof LifecycleProbe) {
            System.out.println("[bean-post-processor] Before initialization: " + beanName);
        }
        return bean;
    }

    // Study note: this hook runs after initialization and may return a wrapper/proxy instead of the bean.
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof LifecycleProbe) {
            System.out.println("[bean-post-processor] After initialization: " + beanName);
        }
        return bean;
    }
}