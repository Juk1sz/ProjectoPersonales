package com.bank.loanorigination;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bank.loanorigination.model.LoanApplication;
import com.bank.loanorigination.repository.LoanApplicationRepository;

@SpringBootTest
class LoanOriginationApplicationTests {

	@Autowired
	private LoanApplicationRepository repository;

	@Test
	void contextLoadsAndSavesEntity() {
		// crea y guarda
		LoanApplication entity = LoanApplication.builder()
				.fullName("Test User")
				.dni("11111111H")
				.email("test@user.com")
				.requestedAmount(BigDecimal.valueOf(1_000))
				.approved(Boolean.TRUE)
				.build();

		UUID id = repository.save(entity).getId();

		// recupera y verifica
		Optional<LoanApplication> foundOpt = repository.findById(id);
		LoanApplication found = foundOpt.orElseThrow(
				() -> new AssertionError("LoanApplication not found"));

		assertThat(found.getFullName()).isEqualTo("Test User");
		assertThat(found.getApproved()).isTrue();
	}
}
