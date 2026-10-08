package com.logondigital.bozacm.DTO.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la mise à jour PARTIELLE du profil admin (PUT /api/v1/admins/update-profile).
 * Tous les champs sont optionnels : seuls ceux fournis sont modifiés.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateDTO {

    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @Email(message = "L'email doit être valide")
    private String email;
}
