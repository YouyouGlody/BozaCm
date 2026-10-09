package com.logondigital.bozacm.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Évolution mensuelle d'une agence et taux de remplissage de ses offres.
 * Sert au graphique de la page « Statistiques » d'une agence.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvolutionAgenceDTO {

    /** Un point par mois, du plus ancien au plus récent (mois sans réservation compris). */
    private List<MoisDTO> mois;

    /** Places proposées sur toutes les offres de l'agence. */
    private long placesTotales;

    /** Places occupées par des réservations non annulées. */
    private long placesReservees;

    /** placesReservees / placesTotales, en pourcentage (0 si aucune place). */
    private double tauxRemplissage;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoisDTO {
        /** Mois au format AAAA-MM, par exemple « 2026-10 ». */
        private String mois;
        private long reservations;
        private long confirmees;
        /** Chiffre d'affaires des réservations confirmées du mois, en FCFA. */
        private double chiffreAffaire;
    }
}
