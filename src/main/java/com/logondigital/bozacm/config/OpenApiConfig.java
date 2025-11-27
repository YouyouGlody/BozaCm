package com.logondigital.bozacm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API BOZACM - Gestion de Voyages")
                        .version("1.0.0")
                        .description("API complète pour la gestion de voyages au Cameroun\n\n" +
                                "**Modules disponibles :**\n" +
                                "- 📋 Gestion des clients\n" +
                                "- 🎫 Gestion des billets (avec QR Code)\n" +
                                "- 🚌 Réservations Bus\n" +
                                "- 🚂 Réservations Train\n" +
                                "- ✈️ Réservations Avion\n" +
                                "- 🏢 Gestion des agences\n" +
                                "- 🎯 Gestion des offres\n" +
                                "- 🛤️ Gestion des trajets\n" +
                                "- 📊 Statistiques")
                        .contact(new Contact()
                                .name("Équipe BozaCM")
                                .email("contact@bozacm.com")
                                .url("https://github.com/YouyouGlody/BozaCm"))
                        .license(new License()
                                .name("Logone Digital")
                                .url("https://logonedigital.com")));
    }
}