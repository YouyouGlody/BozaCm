package com.logondigital.bozacm.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "LOGONEDIGITAL TEKHUB ACADEMY",
                        email = "contact@logonedigital.com",
                        url = "https://logonedigital.com"
                ),
                title = "BozaCM APIs",
                description = "Application web de gestion de voyage au Cameroun",
                termsOfService = "&copy; LOGONEDIGITAL",
                version = "0.0.1-SNAPSHOT"
        )
)
public class OpenApiConfig {

}
