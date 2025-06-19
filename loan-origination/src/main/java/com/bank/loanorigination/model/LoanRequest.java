package com.bank.loanorigination.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoanRequest {
    private double amount;
    private boolean approved;

    public LoanRequest(double amount) {
        this.amount = amount;
        this.approved = false;
    }
}
