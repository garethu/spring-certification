package com.garethyoo.springcertification.lab;

import static org.assertj.core.api.Assertions.assertThat;

import com.garethyoo.springcertification.lab.scope.ConversationProbe;
import com.garethyoo.springcertification.lab.scope.LabScope;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class ScopeBehaviorTest {
    @Autowired
    private PrototypeConsumer prototypeConsumer;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private LabScope labScope;

        @Test
        void containerReturnsSameSingletonAndFreshPrototypeInstances() {
        assertThat(applicationContext.getBean(CatalogService.class))
            .isSameAs(applicationContext.getBean(CatalogService.class));
        assertThat(applicationContext.getBean(PrototypeProbe.class))
            .isNotSameAs(applicationContext.getBean(PrototypeProbe.class));
        }

    @Test
    void prototypeDependencyIsCapturedButProviderCreatesFreshInstances() {
        assertThat(prototypeConsumer.injectedPrototypeId())
                .isEqualTo(prototypeConsumer.injectedPrototypeId());
        assertThat(prototypeConsumer.nextPrototypeId())
                .isNotEqualTo(prototypeConsumer.nextPrototypeId());
    }

    @Test
    void customScopeReusesInstanceUntilConversationEnds() {
        String firstId = applicationContext.getBean(ConversationProbe.class).id();
        assertThat(applicationContext.getBean(ConversationProbe.class).id()).isEqualTo(firstId);

        labScope.endConversation();

        assertThat(applicationContext.getBean(ConversationProbe.class).id()).isNotEqualTo(firstId);
        labScope.endConversation();
    }
}