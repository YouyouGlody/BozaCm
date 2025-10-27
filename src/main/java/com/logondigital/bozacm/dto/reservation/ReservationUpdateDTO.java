package com.logondigital.bozacm.dto.reservation;

import com.logondigital.bozacm.enums.StatutReservation;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO pour la MISE À JOUR d'une réservation (PUT /api/v1/reservations/{id}).

 * TOUS les champs sont OPTIONNELS.
 * Permet un update PARTIEL : seuls les champs fournis sont modifiés.

 * Exemple : Si tu envoies juste {"statutReservation": "CONFIRMEE"},
 * seul le statut change, le reste est conservé.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationUpdateDTO {

    /**
     * Ville de départ (optionnel).
     */
    @Size(min = 2, max = 100, message = "La ville de départ doit contenir entre 2 et 100 caractères")
    private String villeDeDepart;

    /**
     * Ville d'arrivée (optionnel).
     */
    @Size(min = 2, max = 100, message = "La ville d'arrivée doit contenir entre 2 et 100 caractères")
    private String villeArrivee;

    /**
     * Date de départ (optionnel).
     * Si fournie, doit être dans le futur.
     */
    @Future(message = "La date de départ doit être dans le futur")
    private LocalDateTime dateDepart;

    /**
     * Statut de la réservation (optionnel).
     * Valeurs : EN_ATTENTE, CONFIRMEE, ANNULEE, TERMINEE
     */
    private StatutReservation statutReservation;
}