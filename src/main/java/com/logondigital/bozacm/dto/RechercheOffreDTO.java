package com.logondigital.bozacm.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO de critères de recherche d'offres.
 * Tous les champs sont optionnels : seuls les champs non-null
 * sont appliqués comme filtres dans la requête JPQL.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RechercheOffreDTO {

    /** Filtre sur la ville de départ du trajet (insensible à la casse). */
    private String villeDepart;

    /** Filtre sur la ville d'arrivée du trajet (insensible à la casse). */
    private String villeArrivee;

    @Min(value = 0, message = "Le prix minimum ne peut pas être négatif")
    private Double prixMin;

    @Min(value = 0, message = "Le prix maximum ne peut pas être négatif")
    private Double prixMax;

    /** Filtre sur la date de départ de l'offre (>= dateDepart). */
    private LocalDate dateDepart;

    /** Filtre optionnel pour restreindre les résultats à une agence. */
    private Integer agenceId;
}