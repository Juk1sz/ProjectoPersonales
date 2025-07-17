package com.bank.loanorigination.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.bank.loanorigination.model.LoanApplication;

@Repository
public interface LoanApplicationRepository
        extends JpaRepository<LoanApplication, Long>, JpaSpecificationExecutor<LoanApplication> {

}
