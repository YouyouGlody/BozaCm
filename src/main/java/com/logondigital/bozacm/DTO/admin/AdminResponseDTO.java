package com.logondigital.bozacm.DTO.admin;

import com.logondigital.bozacm.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de réponse pour le profil admin (GET /me, PUT /update-profile, POST /photo).
 * Ne contient jamais le mot de passe.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponseDTO {

    private Integer id;
    private String nom;
    private String email;
    private String photoUrl;
    private Role role;
}
