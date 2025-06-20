package com.bank.loanorigination.service;

import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import com.bank.loanorigination.model.LoanApplication;
import com.bank.loanorigination.model.LoanRequest;

@Service
public class LoanEvaluationService {

    private final KieSession kieSession;

    public LoanEvaluationService(KieSession kieSession) {
        this.kieSession = kieSession;
    }

    public LoanRequest evaluate(LoanRequest request) {
        kieSession.insert(request);
        kieSession.fireAllRules();
        return request;
    }

    public LoanApplication createLoan(LoanApplication application){
        s
    }

}
