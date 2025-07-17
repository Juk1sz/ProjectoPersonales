package com.bank.loanorigination.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.bank.loanorigination.model.LoanApplication;
import com.bank.loanorigination.model.LoanRequest;
import com.bank.loanorigination.repository.LoanApplicationRepository;

class LoanApplicationServiceTest {

    @Test
    void evaluatesAndPersists() {
        LoanEvaluationService evalSvc = mock(LoanEvaluationService.class);
        LoanApplicationRepository repo = mock(LoanApplicationRepository.class);
        LoanApplicationService service = new LoanApplicationService(evalSvc, repo);

        LoanApplication entity = LoanApplication.builder()
                .fullName("Ada")
                .dni("12345678Z")
                .email("ada@math.com")
                .requestedAmount(BigDecimal.valueOf(7_000))
                .build();

        when(evalSvc.evaluate(any(LoanRequest.class))).thenReturn(true);
        when(repo.save(any(LoanApplication.class))).thenAnswer(invocation -> {
            LoanApplication saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        LoanApplication saved = service.create(entity);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getApproved()).isTrue(); // <-- getApproved()

        verify(evalSvc).evaluate(any(LoanRequest.class));
        verify(repo).save(saved);
    }
}
