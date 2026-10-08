package com.logondigital.bozacm.service.admin;

import com.logondigital.bozacm.DTO.admin.AdminUpdateDTO;
import com.logondigital.bozacm.entities.Admin;
import com.logondigital.bozacm.exceptions.EmailAlreadyExistsException;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;

/**
 * Logique métier du profil admin : lecture, mise à jour nom/email, mise à jour photo.
 * Toujours résolu via l'email du JWT (pas un {id} dans l'URL) pour qu'un admin
 * ne puisse modifier que son propre profil.
 */
public interface AdminService {

    /**
     * Trouve un admin par son email.
     *
     * @throws RessourceNotFoundException Si l'admin n'existe pas
     */
    Admin findByEmail(String email);

    /**
     * Met à jour nom/email de l'admin authentifié (update partiel).
     *
     * @throws EmailAlreadyExistsException Si le nouvel email est déjà utilisé
     */
    Admin updateProfile(String email, AdminUpdateDTO dto);

    /**
     * Met à jour l'URL de la photo de profil de l'admin authentifié.
     */
    Admin updatePhoto(String email, String photoUrl);
}
