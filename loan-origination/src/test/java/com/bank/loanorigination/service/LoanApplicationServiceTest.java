package com.bank.loanorigination.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.bank.loanorigination.model.LoanApplication;
import com.bank.loanorigination.model.LoanRequest;
import com.bank.loanorigination.repository.LoanApplicationRepository;

@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceTest {
    @InjectMocks
    private LoanApplicationService service;

    @Mock
    private LoanEvaluationService evalSvc;

    @Mock
    private LoanApplicationRepository repo;

    private LoanApplication entity;

    private Pageable pageable;


    @BeforeEach
    void setUp() {
        entity = LoanApplication.builder().fullName("Ada").dni("12345678Z").email("ada@math.com")
                .requestedAmount(BigDecimal.valueOf(7_000)).build();

        pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"));

    }

    @Test
    void evaluatesAndPersists() {
        // Arrange
        when(evalSvc.evaluate(any(LoanRequest.class))).thenReturn(true);
        when(repo.save(any(LoanApplication.class))).thenAnswer(invocation -> {
            LoanApplication saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        // Act
        LoanApplication saved = service.create(entity);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getApproved()).isTrue(); // <-- getApproved()

        verify(evalSvc).evaluate(any(LoanRequest.class));
        verify(repo).save(saved);
    }

    @Test
    void givenNullApproved_whenFindAll_thenCallsFindAllRepo() {
        // Arrange
        Page<LoanApplication> mockPage = Page.empty();
        when(repo.findAll(pageable)).thenReturn(mockPage);

        // Act
        Page<LoanApplication> result = service.findAll(pageable, null);

        // Assert
        assertThat(result).isSameAs(mockPage);
        verify(repo).findAll(pageable);
        verifyNoMoreInteractions(repo);

    }

    @Test
    void givenApproved_whenFindAll_thenCallsFindByApprovedRepo() {
        // Arrange
        Page<LoanApplication> mockPage = Page.empty();
        when(repo.findByApproved(true, pageable)).thenReturn(mockPage);

        // Act
        Page<LoanApplication> result = service.findAll(pageable, true);

        // Assert
        assertThat(result).isSameAs(mockPage);
        verify(repo).findByApproved(true, pageable);
        verifyNoMoreInteractions(repo);
    }
}
