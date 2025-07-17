package com.bank.loanorigination.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.loanorigination.Utils.routesLoanOrigination;
import com.bank.loanorigination.model.LoanApplication;
import com.bank.loanorigination.service.LoanApplicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = routesLoanOrigination.POST_LOAN_APPLICATION)
@Tag(name = "Loan Application", description = "Operationes para solicitar prestamos")
public class LoanApplicationController {

    private final LoanApplicationService service;

    public LoanApplicationController(LoanApplicationService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los prestamos")
    public ResponseEntity<List<LoanApplication>> listAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    @Operation(summary = "Crear una solicitud de préstamo")
    public ResponseEntity<LoanApplication> create(@Valid @RequestBody LoanApplication request) {
        LoanApplication result = service.create(request);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

}