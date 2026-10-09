package com.garethyoo.springcertification.lab;

import com.garethyoo.springcertification.lab.scope.ConversationProbe;
import com.garethyoo.springcertification.lab.scope.LabScope;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DayOneDemo implements CommandLineRunner {
    private final ApplicationContext applicationContext;
    private final BeanFactory beanFactory;
    private final CatalogService catalogService;
    private final PrototypeConsumer prototypeConsumer;
    private final ConversationProbe conversationProbe;
    private final LabScope labScope;

    public DayOneDemo(
            ApplicationContext applicationContext,
            BeanFactory beanFactory,
            CatalogService catalogService,
            PrototypeConsumer prototypeConsumer,
            ConversationProbe conversationProbe,
            LabScope labScope) {
        this.applicationContext = applicationContext;
        this.beanFactory = beanFactory;
        this.catalogService = catalogService;
        this.prototypeConsumer = prototypeConsumer;
        this.conversationProbe = conversationProbe;
        this.labScope = labScope;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=== Day 1: Spring Container Lab ===");
        System.out.println("\n[1. Container APIs]");
        System.out.println("ApplicationContext is a BeanFactory: " + (applicationContext instanceof BeanFactory));
        System.out.println("BeanFactory lookup: " + beanFactory.getBean("catalogService").getClass().getSimpleName());

        System.out.println("\n[2. Constructor injection]");
        System.out.println("Constructor injection: " + catalogService.describeInventory());

        System.out.println("\n[3. Singleton vs prototype lookups]");
        CatalogService singletonFirst = applicationContext.getBean(CatalogService.class);
        CatalogService singletonSecond = applicationContext.getBean(CatalogService.class);
        System.out.println("Two CatalogService lookups return the same singleton: "
                + (singletonFirst == singletonSecond));
        PrototypeProbe prototypeFirst = applicationContext.getBean(PrototypeProbe.class);
        PrototypeProbe prototypeSecond = applicationContext.getBean(PrototypeProbe.class);
        System.out.println("Two PrototypeProbe lookups return different instances: "
                + (prototypeFirst != prototypeSecond));

        System.out.println("\n[4. Prototype injected into a singleton]");
        String capturedId = prototypeConsumer.injectedPrototypeId();
        System.out.println("Prototype injected once into singleton; repeated access keeps its id: "
                + capturedId.equals(prototypeConsumer.injectedPrototypeId()));
        System.out.println("ObjectProvider.getObject() returns a fresh prototype each call: "
                + !prototypeConsumer.nextPrototypeId().equals(prototypeConsumer.nextPrototypeId()));

        System.out.println("\n[5. Custom 'lab' scope]");
        String conversationFirst = conversationProbe.id();
        String conversationSecond = applicationContext.getBean(ConversationProbe.class).id();
        System.out.println("Custom 'lab' scope reuses instance in this conversation: "
                + conversationFirst.equals(conversationSecond));
        labScope.endConversation();
        String nextConversation = applicationContext.getBean(ConversationProbe.class).id();
        System.out.println("Custom 'lab' scope creates a new instance after conversation ends: "
                + !conversationFirst.equals(nextConversation));
        labScope.endConversation();

        System.out.println("\n[6. Lifecycle]");
        System.out.println("See LifecycleProbe startup logs for constructor, @PostConstruct, and post-processors.");
        System.out.println("=== Close the application to observe @PreDestroy ===\n");
    }
}