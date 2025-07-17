package com.bank.loanorigination.service;

import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import com.bank.loanorigination.model.LoanRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanEvaluationService {

    private final KieSession kieSession;

    public boolean evaluate(LoanRequest request) {
        kieSession.insert(request); // 1. mete el hecho
        kieSession.fireAllRules(); // 2. dispara las reglas
        return Boolean.TRUE.equals(request.getApproved()); // 3. lee el resultado
    }

}
