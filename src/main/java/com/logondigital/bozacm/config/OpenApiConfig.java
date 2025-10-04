package com.logondigital.bozacm.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Configuration de la documentation Swagger/OpenAPI pour le projet BozaCM.
 * Cette classe permet d'afficher automatiquement la documentation interactive
 * des endpoints REST via Swagger UI (accessible par défaut sur /swagger-ui.html).
 */
@OpenAPIDefinition(
        info = @Info(
                title = "BozaCM - API Documentation",
                version = "v1",
                description = "API pour la gestion des réservations, billets et clients du projet BozaCM.",
                contact = @Contact(
                        name = "LOGONEDIGITAL TEKHUB ACADEMY",
                        email = "contact@logonedigital.com",
                        url = "https://logonedigital.com"
                ),
                termsOfService = "© 2025 LOGONEDIGITAL TEKHUB ACADEMY - Tous droits réservés."
        )
)

public class OpenApiConfig {
}
