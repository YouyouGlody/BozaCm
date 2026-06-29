package com.logondigital.bozacm.DTO.reservation.bus;

import com.logondigital.bozacm.enums.transport.TypeBus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour créer une nouvelle réservation de bus.
 * Le client choisit une OFFRE (qui fixe trajet, prix, date) + ses infos de transport.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationBusRequestDTO {

    // ==================== LIEN VERS L'OFFRE ====================

    /**
     * ID de l'offre réservée. C'est elle qui fournit le trajet, le prix et la date.
     */
    @NotNull(message = "L'ID de l'offre est obligatoire")
    @Positive(message = "L'ID de l'offre doit être positif")
    private Integer offreId;

    /**
     * ID du client qui effectue la réservation
     */
    @NotNull(message = "L'ID du client est obligatoire")
    @Positive(message = "L'ID du client doit être positif")
    private Integer clientId;

    // ==================== CHAMPS SPÉCIFIQUES AU BUS ====================

    @NotNull(message = "Le nom de la compagnie de bus est obligatoire")
    @NotBlank(message = "Le nom de la compagnie ne doit pas être vide")
    @Size(min = 2, max = 100, message = "Le nom de la compagnie doit contenir entre 2 et 100 caractères")
    private String compagnieBus;

    @NotNull(message = "Le type de bus est obligatoire")
    private TypeBus typeBus;

    @NotNull(message = "L'information sur la climatisation est obligatoire")
    private Boolean climatisation;
}