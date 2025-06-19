package com.bank.loanorigination.config;

import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfig {

    @Bean
    public KieSession kieSession() {
        KieHelper kieHelper = new KieHelper();
        kieHelper.addResource(
                org.kie.internal.io.ResourceFactory.newClassPathResource("rules/loan-rules.drl"),
                org.kie.api.io.ResourceType.DRL);
        return kieHelper.build().newKieSession();
    }
}
