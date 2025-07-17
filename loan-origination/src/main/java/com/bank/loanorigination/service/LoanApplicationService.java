package com.bank.loanorigination.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bank.loanorigination.model.LoanApplication;
import com.bank.loanorigination.model.LoanRequest;
import com.bank.loanorigination.repository.LoanApplicationRepository;

@Service
public class LoanApplicationService {

    private final LoanApplicationRepository repository;
    private final LoanEvaluationService evaluationService;

    public LoanApplicationService(LoanApplicationRepository repository, LoanEvaluationService evaluationService) {
        this.repository = repository;
        this.evaluationService = evaluationService;
    }

    public Page<LoanApplication> findAll(Pageable pageable, Boolean approved) {
        Pageable fixed = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC,
                "createdAt");

                if(approved == null) {
                    return repo.findAll(fixed)
                }

    }

    public LoanApplication create(LoanApplication application) {
        // Convertimos LoanApplication a LoanRequest
        LoanRequest request = new LoanRequest(application.getRequestedAmount().doubleValue());

        // Evaluamos con Drools
        LoanRequest result = evaluationService.evaluate(request);

        // Aplicamos el resultado
        application.setApproved(result.isApproved());

        // Guardamos en base de datos
        return repository.save(application);
    }
}
