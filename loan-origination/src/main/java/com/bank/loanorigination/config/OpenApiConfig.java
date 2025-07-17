package com.bank.loanorigination.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI loanOriginationOpenAPI() {
                return new OpenAPI().info(new Info()
                                .title("Loan Origination API")
                                .version("v1")
                                .description("API Health-check y creación de solicitudes (Tasks 1-3).")
                                .contact(new Contact().name("Dev Team").email("dev-team@example.com"))
                                .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")));
        }
}
