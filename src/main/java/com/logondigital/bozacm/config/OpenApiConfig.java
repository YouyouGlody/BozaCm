package com.logondigital.bozacm.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "WAFO VIANEY",
                        email = "wafovianey2@gmail.com",
                        url = "https://logonedigital.com"
                ),
                title = "GESTION TRAJET APIs",
                description = "Gestion Trajet App APIs ",
                termsOfService = "&copy; LOGONEDIGITAL",
                version = "v1"
        )
)
public class OpenApiConfig {
}