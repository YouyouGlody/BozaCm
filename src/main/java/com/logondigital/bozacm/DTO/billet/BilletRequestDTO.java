package com.logondigital.bozacm.DTO.billet;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la création d'un nouveau billet.
 
 * Le système génère automatiquement :
 *       - Le numéro de billet (BZC-UUID)
 *       - Le statut (VALIDE par défaut)
 *       - Les dates d'émission et d'expiration
 *       - Le QR code
 *      - Le snapshot du client (nom/prénom sur le billet).

 * Le client fournit uniquement les IDs nécessaires.

 * Utilisé dans : POST /api/v1/billets
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BilletRequestDTO {

    /**
     * ID de la réservation pour laquelle le billet est émis.
     * Obligatoire, doit correspondre à une réservation confirmée.
     */
    @NotNull(message = "L'ID de la réservation est obligatoire")
    @Positive(message = "L'ID de la réservation doit être un nombre positif")
    private Integer reservationId;

    /**
     * ID du client qui possède le billet.
     * Obligatoire, doit correspondre au client de la réservation.
     */
    @NotNull(message = "L'ID du client est obligatoire")
    @Positive(message = "L'ID du client doit être un nombre positif")
    private Integer clientId;
}
