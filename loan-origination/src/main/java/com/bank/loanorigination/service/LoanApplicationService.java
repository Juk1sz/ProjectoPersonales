package com.bank.loanorigination.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        LoanRequest dto =
                LoanRequest.builder().requestedAmount(entity.getRequestedAmount()).build();

        boolean approved = evaluationService.evaluate(dto);
        entity.setApproved(approved);
        entity.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        return repository.save(entity);
    }

    public Page<LoanApplication> findAll(Pageable pageable, Boolean approved) {
        /*
         * Esto es como lo hice yo a la primera:
         * 
         * if (approved == null) { return repository.findAll(pageable); } else { return
         * repository.findByApproved(approved, pageable); }
         */
        // Esto es lo mas limpio posible
        return (approved == null) ? repository.findAll(pageable)
                : repository.findByApproved(approved, pageable);
    }
}
