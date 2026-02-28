package com.logondigital.bozacm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de statistiques détaillées d'une Agence.
 * Regroupe les informations de l'agence, ses statistiques d'offres,
 * de réservations et financières, ainsi que son classement.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatistiquesAgenceDetailDTO {

    // ─── Informations de l'agence ──────────────────────────────────────────────
    private Integer agenceId;
    private String  agenceNom;
    private String  agenceEmail;
    private String  agenceTelephone;
    private String  agenceAdresse;

    // ─── Statistiques des offres ───────────────────────────────────────────────
    private Long   nombreOffres;
    private Double prixMoyen;
    private Double prixMin;
    private Double prixMax;

    // ─── Statistiques des réservations ────────────────────────────────────────
    private Long   nombreReservationsTotal;
    private Long   nombreReservationsConfirmees;
    /** Taux en % : (confirmées / total) * 100. Calculé côté service. */
    private Double tauxConfirmation;

    // ─── Statistiques financières ──────────────────────────────────────────────
    /** Chiffre d'affaires : somme des prix des offres avec réservations confirmées. */
    private Double chiffreAffaire;

    // ─── Classement ───────────────────────────────────────────────────────────
    /** Rang de l'agence parmi toutes les agences (trié par chiffreAffaire DESC). */
    private Integer rang;
}