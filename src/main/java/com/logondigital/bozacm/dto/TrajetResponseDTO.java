package com.logondigital.bozacm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de réponse pour un Trajet.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrajetResponseDTO {

    private Integer id;
    private String villeDepart;
    private String villeArrivee;
    private String duree;
}