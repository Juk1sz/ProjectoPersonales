package com.bank.loanorigination.controller;

import static com.bank.loanorigination.Utils.ApiPaths.LOAN_APPLICATIONS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bank.loanorigination.Utils.ApiPaths;
import com.bank.loanorigination.model.LoanApplication;
import com.bank.loanorigination.service.LoanApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
public class LoanApplicationController {

    private final LoanApplicationService service;

    @PostMapping(LOAN_APPLICATIONS)
    @Operation(summary = "Crear una nueva solicitud de préstamo")
    public ResponseEntity<LoanApplication> create(@Valid @RequestBody LoanApplication body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(body));
    }

    @GetMapping(ApiPaths.LOAN_APPLICATIONS)
    @Operation(summary = "Listar solicitudes de prestamos con paginacion y filtro opcional")
    public Page<LoanApplication> listAll(
            @Parameter(description = "Numero de pagina (0-n)",
                    example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de pagina (maximo 100))",
                    example = "20") @RequestParam(defaultValue = "20") @Max(100) int size,
            @Parameter(description = "Filtrar por aprobado, (true-false)",
                    example = "true") @RequestParam(required = false) Boolean approved) {
        Sort sortFijo = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sortFijo);

        return service.findAll(pageable, approved);
    }
}
