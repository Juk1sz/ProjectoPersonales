package com.bank.loanorigination.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.bank.loanorigination.model.LoanApplication;
import com.bank.loanorigination.model.LoanRequest;
import com.bank.loanorigination.repository.LoanApplicationRepository;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class LoanApplicationService {

    private final LoanEvaluationService evaluationService;
    private final LoanApplicationRepository repository;

    public LoanApplication create(LoanApplication entity) {
        LoanRequest dto = LoanRequest.builder()
                .requestedAmount(entity.getRequestedAmount())
                .build();

        boolean approved = evaluationService.evaluate(dto);
        entity.setApproved(approved);
        entity.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        return repository.save(entity);
    }
}
