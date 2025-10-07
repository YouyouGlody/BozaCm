package com.logondigital.bozacm.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Configuration OpenAPI (Swagger)
 * --------------------------------
 * Ce fichier configure la documentation Swagger UI pour ton projet BozaCM.
 * Swagger permet de visualiser, tester et documenter les API REST.
 *
 * Une fois ton application lancée, tu pourras accéder à :
 * 👉 http://localhost:8080/swagger-ui/index.html
 */
@OpenAPIDefinition(
        info = @Info(
                title = "BOZACM APIs",
                version = "v1",
                description = "Documentation des APIs de l’application web BozaCM pour la gestion et la réservation de billets de voyage au Cameroun.",
                termsOfService = "https://bozacm.com/terms",
                contact = @Contact(
                        name = "Équipe BozaCM",
                        email = "support@bozacm.com",
                        url = "https://bozacm.com"
                )
        )
)
public class OpenApiConfig {
    // Aucun code nécessaire ici, les annotations suffisent 💡
}

