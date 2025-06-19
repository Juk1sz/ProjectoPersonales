package com.bank.loanorigination.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import com.bank.loanorigination.model.LoanRequest;

class LoanEvaluationServiceTest {

    private LoanEvaluationService service;

    @BeforeEach
    void setUp() {
        KieHelper kieHelper = new KieHelper();
        kieHelper.addResource(
                org.kie.internal.io.ResourceFactory.newClassPathResource("rules/loan-rules.drl"),
                org.kie.api.io.ResourceType.DRL);
        KieSession kieSession = kieHelper.build().newKieSession();
        service = new LoanEvaluationService(kieSession);
    }

    @Test
    void evaluate_OK() {
        LoanRequest request = new LoanRequest(5000);
        LoanRequest result = service.evaluate(request);
        assertTrue(result.isApproved(), "Loan should be approved");
    }

    @Test
    void evaluate_KO() {
        LoanRequest request = new LoanRequest(15000);
        LoanRequest result = service.evaluate(request);
        assertFalse(result.isApproved(), "Loan should NOT be approved");
    }
}
