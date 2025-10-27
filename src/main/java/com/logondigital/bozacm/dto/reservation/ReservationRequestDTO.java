package com.logondigital.bozacm.dto.reservation;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO pour la CRÉATION d'une réservation (POST /api/v1/reservations).

 * Le client fournit :
 * - Les détails du voyage (villes, date)
 * - Son ID

 * Le système génère automatiquement :
 * - idReservation (BDD)
 * - statutReservation (EN_ATTENTE par défaut)
 * - createdAt, updatedAt (@PrePersist, @PreUpdate)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDTO {

    /**
     * Ville de départ.
     * Obligatoire, 2-100 caractères.
     */
    @NotBlank(message = "La ville de départ est obligatoire")
    @Size(min = 2, max = 100, message = "La ville de départ doit contenir entre 2 et 100 caractères")
    private String villeDeDepart;

    /**
     * Ville d'arrivée.
     * Obligatoire, 2-100 caractères.
     */
    @NotBlank(message = "La ville d'arrivée est obligatoire")
    @Size(min = 2, max = 100, message = "La ville d'arrivée doit contenir entre 2 et 100 caractères")
    private String villeArrivee;

    /**
     * Date et heure de départ prévues.
     * Obligatoire, doit être dans le futur.
     */
    @NotNull(message = "La date de départ est obligatoire")
    @Future(message = "La date de départ doit être dans le futur")
    private LocalDateTime dateDepart;

    /**
     * ID du client qui fait la réservation.
     * Obligatoire, doit correspondre à un client existant.
     */
    @NotNull(message = "L'ID du client est obligatoire")
    @Positive(message = "L'ID du client doit être un nombre positif")
    private Integer clientId;
}
