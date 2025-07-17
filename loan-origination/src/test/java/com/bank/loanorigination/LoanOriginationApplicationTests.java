package com.bank.loanorigination;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.bank.loanorigination.model.LoanApplication;
import com.bank.loanorigination.repository.LoanApplicationRepository;

@DataJpaTest // levanta sólo la capa JPA + H2
@TestPropertySource(properties = // fuerza UTC para Hibernate
"spring.jpa.properties.hibernate.jdbc.time_zone=UTC")
class LoanApplicationRepositoryTest {

	@Autowired
	private LoanApplicationRepository repo;

	@Test
	void createdAtShouldBeUtc() {
		// Arrange
		LoanApplication app = new LoanApplication();
		app.setFullName("Demo");
		app.setRequestedAmount(BigDecimal.TEN);

		// Act
		repo.save(app);
		OffsetDateTime stored = repo.findById(app.getId())
				.orElseThrow()
				.getCreatedAt();

		// Assert
		assertEquals(ZoneOffset.UTC, stored.getOffset(),
				"createdAt debe persistirse con sufijo Z (UTC)");
	}
}
