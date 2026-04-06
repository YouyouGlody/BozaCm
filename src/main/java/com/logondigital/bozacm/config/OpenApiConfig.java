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
                        .title("BOZACM – Gestion des Trajets & Voyages")
                        .version("v1")
                        .description("""
                                Application de gestion de voyages au Cameroun.

                                Modules disponibles :
                                - 📋 Gestion des clients
                                - 🎫 Gestion des billets (QR Code)
                                - 🚌 Réservations Bus
                                - 🚂 Réservations Train
                                - ✈️ Réservations Avion
                                - 🛤️ Gestion des trajets
                                - 📊 Statistiques
                                """)
                        .contact(new Contact()
                                .name("WAFO VIANEY / Équipe BozaCM")
                                .email("wafovianey2@gmail.com")
                                .url("https://logonedigital.com"))
                        .license(new License()
                                .name("Logone Digital Tekhub Academy")
                                .url("https://logonedigital.com"))
                );
    }
}
}