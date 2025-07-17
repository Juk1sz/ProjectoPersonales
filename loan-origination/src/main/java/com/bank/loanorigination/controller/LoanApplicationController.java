package com.bank.loanorigination.controller;

import static com.bank.loanorigination.Utils.ApiPaths.LOAN_APPLICATIONS;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bank.loanorigination.model.LoanApplication;
import com.bank.loanorigination.service.LoanApplicationService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LoanApplicationController {

    private final LoanApplicationService service;

    @PostMapping(LOAN_APPLICATIONS)
    @Operation(summary = "Crear una nueva solicitud de préstamo")
    public ResponseEntity<LoanApplication> create(@Valid @RequestBody LoanApplication body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(body));
    }
}
