package com.example.springcertification.day2;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DependencyInjectionTest {
    @Autowired
    private ConstructorInjectionExample constructorExample;

    @Autowired
    private SetterInjectionExample setterExample;

    @Autowired
    private FieldInjectionExample fieldExample;

    @Autowired
    private NotificationService notificationService;

    @Test
    void injectsCollaboratorsUsingConstructorSetterAndFieldInjection() {
        assertThat(constructorExample.message()).isEqualTo("The dependency was injected.");
        assertThat(setterExample.message()).isEqualTo("The dependency was injected.");
        assertThat(fieldExample.message()).isEqualTo("The dependency was injected.");
    }

    @Test
    void selectsPrimaryBeanByDefaultAndQualifierByName() {
        assertThat(notificationService.sendWithPrimary("hello")).isEqualTo("email: hello");
        assertThat(notificationService.sendWithSms("hello")).isEqualTo("sms: hello");
    }
}