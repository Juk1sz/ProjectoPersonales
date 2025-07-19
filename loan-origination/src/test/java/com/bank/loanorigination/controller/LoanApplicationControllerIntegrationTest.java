package com.bank.loanorigination.controller;

import static com.bank.loanorigination.Utils.ApiPaths.LOAN_APPLICATIONS;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.bank.loanorigination.model.LoanApplication;
import com.bank.loanorigination.repository.LoanApplicationRepository;



@SpringBootTest
@AutoConfigureMockMvc
class LoanApplicationControllerIntegrationTest {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private LoanApplicationRepository repo;

        private LoanApplication baseLoan(String fullName, String dni, boolean approved,
                        OffsetDateTime createdAt) {
                return LoanApplication.builder().fullName(fullName).dni(dni)
                                .email(fullName.toLowerCase() + "@email.com")
                                .requestedAmount(BigDecimal.valueOf(1000)).approved(approved)
                                .createdAt(createdAt).build();
        }

        @BeforeEach
        void setUp() {
                repo.deleteAll();
        }

        @Test
        void givinNoData_whenGetLoanApplications_thenReturnsEmptyList() throws Exception {
                // Act & Assert
                mvc.perform(get(LOAN_APPLICATIONS).contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content", hasSize(0)));

        }

        @Test
        void givinData_whenGetLoanApplications_thenReturnList() throws Exception {
                // Arrange
                LoanApplication one = baseLoan("One", "12345678A", true,
                                Instant.parse("2025-07-18T10:00:00Z").atOffset(ZoneOffset.UTC));
                LoanApplication two = baseLoan("Two", "23456789B", true,
                                Instant.parse("2025-07-18T08:00:00Z").atOffset(ZoneOffset.UTC));
                LoanApplication three = baseLoan("Three", "34567899C", false,
                                Instant.parse("2025-07-18T12:00:00Z").atOffset(ZoneOffset.UTC));



                repo.saveAll(List.of(one, two, three));

                // Act & Assert
                mvc.perform(get(LOAN_APPLICATIONS).param("page", "0").param("size", "5")
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                                .andExpect(jsonPath("$.content", hasSize(3)))
                                .andExpect(jsonPath("$.content[0].fullName", is("Three")))
                                .andExpect(jsonPath("$.content[1].fullName", is("One")))
                                .andExpect(jsonPath("$.content[2].fullName", is("Two")));
        }

        @Test
        void givinApproved_whenGetLoanApplications_thenReturnOnlyApproved() throws Exception {
                // Arrange

                LoanApplication one = baseLoan("One", "12345678A", true,
                                Instant.parse("2025-07-18T10:00:00Z").atOffset(ZoneOffset.UTC));
                LoanApplication two = baseLoan("Two", "23456789B", true,
                                Instant.parse("2025-07-18T08:00:00Z").atOffset(ZoneOffset.UTC));
                LoanApplication three = baseLoan("Three", "34567899C", false,
                                Instant.parse("2025-07-18T12:00:00Z").atOffset(ZoneOffset.UTC));


                repo.saveAll(List.of(one, two, three));

                // Act & Assert
                mvc.perform(get(LOAN_APPLICATIONS).param("approved", "true")
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                                .andExpect(jsonPath("$.content", hasSize(2)))
                                .andExpect(jsonPath("$.content[*].approved",
                                                containsInAnyOrder(true, true)))
                                .andExpect(jsonPath("$.content[*].fullName",
                                                containsInAnyOrder("One", "Two")));

        }

        @Test
        void givenSizeGreaterThan100_whenGetLoanApplications_thenReturns400() throws Exception {
                mvc.perform(get(LOAN_APPLICATIONS).param("page", "0").param("size", "150")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$['size']").exists());
        }


}
