package com.logondigital.bozacm.dto.client;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la MISE À JOUR d'un client existant (PUT /api/v1/clients/{id}).

 * TOUS les champs sont OPTIONNELS.
 * Permet un update PARTIEL : seuls les champs fournis sont modifiés.

 * Exemple : Si tu envoies juste {"email": "new@test.com"},
 * seul l'email change, le reste est conservé.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateDTO {

    /**
     * Nom du client (optionnel).
     * Si fourni, doit respecter 2-50 caractères.
     */
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String nom;

    /**
     * Prénom du client (optionnel).
     */
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    private String prenom;

    /**
     * Email du client (optionnel).
     * Si fourni, doit être valide.
     */
    @Email(message = "L'email doit être valide")
    private String email;

    /**
     * Numéro de téléphone (optionnel).
     * Si fourni, doit respecter le format camerounais.
     */
    @Pattern(
            regexp = "^6[0-9]{8}$",
            message = "Le numéro doit commencer par 6 et contenir 9 chiffres"
    )
    private String numeroTelephone;

    /**
     * Adresse du client (optionnel).
     */
    @Size(max = 200, message = "L'adresse ne doit pas dépasser 200 caractères")
    private String adresse;
}