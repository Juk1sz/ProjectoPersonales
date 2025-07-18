package com.bank.loanorigination.controller;

import static com.bank.loanorigination.Utils.ApiPaths.LOAN_APPLICATIONS;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import com.bank.loanorigination.exception.GlobalExceptionHandler;
import com.bank.loanorigination.model.LoanApplication;
import com.bank.loanorigination.service.LoanApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class LoanApplicationControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Mock
    private LoanApplicationService service;

    @InjectMocks
    private LoanApplicationController controller;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()).setValidator(validator).build();
    }

    @Test
    void givenValidRequest_whenCreate_thenReturns201() throws Exception {
        // Arrange
        LoanApplication req = LoanApplication.builder().fullName("Grace").dni("87654321X")
                .email("grace@uni.com").requestedAmount(BigDecimal.valueOf(3_000)).build();

        when(service.create(any())).thenReturn(req.toBuilder().approved(true).build());

        // Act & Assert
        mvc.perform(post(LOAN_APPLICATIONS).contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(req))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.approved", is(true)));

        verify(service).create(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void givenInvalidRequest_whenCreate_thenReturns400() throws Exception {
        // Arrange
        String invalidJson =
                "{ \"fullName\": \"\", \"dni\": \"bad\", \"email\": \"x@\", \"requestedAmount\": -1 }";

        // Act & Assert
        mvc.perform(post(LOAN_APPLICATIONS).contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fullName").exists());

        verifyNoMoreInteractions(service);
    }

}
