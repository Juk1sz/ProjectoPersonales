package com.bank.loanorigination.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.bank.loanorigination.model.LoanApplication;

@Repository
public interface LoanApplicationRepository
                extends JpaRepository<LoanApplication, UUID>,
                JpaSpecificationExecutor<LoanApplication> {

        Page<LoanApplication> findByApproved(Boolean approved, Pageable pageable);
}
