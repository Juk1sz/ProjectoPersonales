package com.bank.loanorigination.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.drools.core.io.impl.ClassPathResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import com.bank.loanorigination.model.LoanRequest;

class LoanEvaluationServiceTest {

    private LoanEvaluationService service;

    @BeforeEach
    void setUp() {
        // Carga la regla desde src/main/resources/rules/loan-rules.drl
        KieHelper kieHelper = new KieHelper()
                .addResource(new ClassPathResource("rules/loan-rules.drl"));

        KieSession kieSession = kieHelper.build().newKieSession();
        service = new LoanEvaluationService(kieSession);
    }

    @Test
    void approvesBelowLimit() {
        LoanRequest request = LoanRequest.builder()
                .requestedAmount(BigDecimal.valueOf(5_000))
                .build(); // approved = false por defecto

        boolean ok = service.evaluate(request);

        assertThat(ok).isTrue();
    }

    @Test
    void rejectsAboveLimit() {
        LoanRequest request = LoanRequest.builder()
                .requestedAmount(BigDecimal.valueOf(15_000))
                .build();

        boolean ok = service.evaluate(request);

        assertThat(ok).isFalse();
    }
}
