package com.logondigital.bozacm.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de création / mise à jour d'un Trajet.
 * Note : les champs sont renommés villeDepart/villeArrivee
 * pour correspondre à la sémantique métier du projet.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrajetRequestDTO {

    @NotBlank(message = "La ville de départ est obligatoire")
    private String villeDepart;

    @NotBlank(message = "La ville d'arrivée est obligatoire")
    private String villeArrivee;

    @NotBlank(message = "La durée est obligatoire")
    private String duree;
}