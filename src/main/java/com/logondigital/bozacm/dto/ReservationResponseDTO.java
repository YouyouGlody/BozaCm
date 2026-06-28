package com.logondigital.bozacm.DTO;

import com.logondigital.bozacm.entities.ReservationOffre.StatutReservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO de réponse pour une Réservation.
 * Inclut l'offre complète (avec agence et trajet) via OffreResponseDTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {

    private Integer id;
    private String nomClient;
    private String emailClient;
    private LocalDate dateReservation;

    /** Statut typé via enum pour garantir les valeurs possibles. */
    private StatutReservation statut;

    /** Offre associée, avec ses données d'agence et de trajet. */
    private OffreResponseDTO offre;
}