package com.bank.loanorigination.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * Configura Springdoc-OpenAPI para exponer la UI de Swagger en
 * <code>http://localhost:8080/swagger-ui.html</code> y el contrato en
 * <code>/v3/api-docs</code>.
 *
 * <p>
 * Si en el futuro añadimos seguridad (JWT, OAuth2, etc.) bastará con
 * registrar un {@link io.swagger.v3.oas.models.security.SecurityScheme}
 * en el {@link OpenAPI} que se devuelve aquí.
 * </p>
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI loanAppOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Loan Application API")
                        .version("v1")
                        .description("""
                                API del sistema de solicitudes de préstamo.
                                Incluye endpoints de health check, creación de solicitudes
                                y (próximamente) listado y filtrado con paginación.
                                """)
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("dev-team@example.com")
                                .url("https://github.com/example/loan-app"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
