package com.logondigital.bozacm.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de réponse pour une Agence.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgenceResponseDTO {

    private Integer id;
    private String nom;
    private String email;
    private String telephone;
    private String adresse;
}