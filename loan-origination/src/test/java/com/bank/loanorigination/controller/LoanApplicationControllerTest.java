package com.bank.loanorigination.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bank.loanorigination.Utils.routesLoanOrigination;
import com.bank.loanorigination.model.LoanApplication;
import com.bank.loanorigination.service.LoanApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class LoanApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Inyectamos un mock para el service en el contexto
    @MockBean
    private LoanApplicationService service;

    private LoanApplication input;
    private String fullName = "Ana López";
    private String dni = "11223344X";
    private String email = "ana@example.com";
    private BigDecimal amount = BigDecimal.valueOf(8000);

    @BeforeEach
    void setUp() {
        // Preparamos el objeto que usaremos en los tests
        input = new LoanApplication();
        input.setFullName(fullName);
        input.setDni(dni);
        input.setEmail(email);
        input.setRequestedAmount(amount);
    }

    @Test
    void create_OK_shouldReturn201AndBody() throws Exception {
        // Simulamos que el service aprueba la solicitud
        LoanApplication saved = new LoanApplication();
        saved.setId(1L);
        saved.setFullName(fullName);
        saved.setDni(dni);
        saved.setEmail(email);
        saved.setRequestedAmount(amount);
        saved.setApproved(true);

        when(service.create(any(LoanApplication.class))).thenReturn(saved);

        mockMvc.perform(post(routesLoanOrigination.POST_LOAN_APPLICATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.approved").value(true))
                .andExpect(jsonPath("$.fullName").value(fullName));
    }

    @Test
    void create_KO_shouldReturn400() throws Exception {
        input.setRequestedAmount(null);

        mockMvc.perform(post(routesLoanOrigination.POST_LOAN_APPLICATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

}
