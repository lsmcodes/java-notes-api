package io.github.lsmcodes.notes_api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfiguration {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Notes API").description(
                        "A notes API created as a challenge for the \"Publicando Sua API REST na Nuvem Usando Spring Boot 3, Java 17 e Railway\" course by DIO."))
                .components(new Components().addSecuritySchemes("JWT token",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

}