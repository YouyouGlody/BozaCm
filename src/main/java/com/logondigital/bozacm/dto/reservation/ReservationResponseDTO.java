package com.logondigital.bozacm.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.logondigital.bozacm.enums.StatutReservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO pour la RÉPONSE contenant les données d'une réservation.

 * Inclut :
 *  Tous les détails de la réservation
 * - Les infos essentielles du client (ID + nom/prénom/email)
 * - Les dates de gestion

 * N'inclut PAS l'objet Client complet (optimisation).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {

    /**
     * ID de la réservation (généré par la BDD).
     */
    private Integer idReservation;

    /**
     * Ville de départ.
     */
    private String villeDeDepart;

    /**
     * Ville d'arrivée.
     */
    private String villeArrivee;

    /**
     * Date et heure de départ.
     * Format : dd/MM/yyyy HH:mm
     */
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dateDepart;

    /**
     * Statut de la réservation.
     * Valeurs : EN_ATTENTE, CONFIRMEE, ANNULEE, TERMINEE
     */
    private StatutReservation statutReservation;

    // ════════════════════════════════════════════════════════════════════════
    // INFOS CLIENT (sans charger l'objet complet)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * ID du client.
     */
    private Integer clientId;

    /**
     * Nom du client (pour affichage rapide).
     */
    private String nomClient;

    /**
     * Prénom du client (pour affichage rapide).
     */
    private String prenomClient;

    /**
     * Email du client (pour contact).
     */
    private String emailClient;

    // ════════════════════════════════════════════════════════════════════════
    // DATES DE GESTION
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Date de création de la réservation.
     * Format : dd/MM/yyyy HH:mm:ss
     */
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * Date de dernière modification.
     * Format : dd/MM/yyyy HH:mm:ss
     */
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
}