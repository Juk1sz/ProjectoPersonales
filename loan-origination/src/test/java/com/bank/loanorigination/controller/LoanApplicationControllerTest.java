package com.bank.loanorigination.controller;

import static com.bank.loanorigination.Utils.ApiPaths.LOAN_APPLICATIONS;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.bank.loanorigination.exception.GlobalExceptionHandler;
import com.bank.loanorigination.model.LoanApplication;
import com.bank.loanorigination.service.LoanApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;

class LoanApplicationControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private AutoCloseable closeable;

    @Mock
    private LoanApplicationService service;
    @InjectMocks
    private LoanApplicationController controller;

    private MockMvc mvc;

    @BeforeEach
    void open() {
        closeable = MockitoAnnotations.openMocks(this);

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet(); // inicializa Hibernate Validator

        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator) // Spring Validator correcto
                .build();
    }

    @AfterEach
    void close() throws Exception {
        closeable.close();
    }

    @Test
    void returns201WhenCreated() throws Exception {
        LoanApplication req = LoanApplication.builder()
                .fullName("Grace")
                .dni("87654321X")
                .email("grace@uni.com")
                .requestedAmount(BigDecimal.valueOf(3_000))
                .build();

        when(service.create(any()))
                .thenReturn(req.toBuilder().approved(true).build());

        mvc.perform(post(LOAN_APPLICATIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.approved", is(true)));
    }

    @Test
    void returns400OnValidationError() throws Exception {
        // JSON mal formado para disparar @Valid
        String invalidJson = "{ \"fullName\": \"\", \"dni\": \"bad\", \"email\": \"x@\", \"requestedAmount\": -1 }";

        mvc.perform(post(LOAN_APPLICATIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fullName").exists());
    }
}
