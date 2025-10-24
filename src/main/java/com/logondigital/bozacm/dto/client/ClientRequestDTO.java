package com.logondigital.bozacm.dto.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la création d'un nouveau client.

 * Contient uniquement les champs que le client peut fournir lors de l'inscription.
 * Les champs auto-générés (ID, dates) sont exclus.

 * Utilisé dans : POST /api/v1/clients
 */
@Data                     // Génère getters, setters, toString, equals, hashCode
@NoArgsConstructor       // Constructeur vide (pour Jackson)
@AllArgsConstructor     // Constructeur avec tous les paramètres
public class ClientRequestDTO {

    /**
     * Nom du client.
     * Obligatoire, entre 2 et 50 caractères.
     */
    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String nom;

    /**
     * Prénom du client.
     * Obligatoire, entre 2 et 50 caractères.
     */
    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    private String prenom;

    /**
     * Email du client.
     * Obligatoire, doit être valide et unique.
     */
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide (ex: jean@example.com)")
    private String email;

    /**
     * Numéro de téléphone camerounais.
     * Format attendu : 6XXXXXXXX (9 chiffres commençant par 6)
     */
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(
            regexp = "^6[0-9]{8}$",
            message = "Le numéro doit commencer par 6 et contenir 9 chiffres (ex: 612345678)"
    )
    private String numeroTelephone;

    /**
     * Adresse complète du client.
     * Optionnel, maximum 200 caractères.
     */
    @Size(max = 200, message = "L'adresse ne doit pas dépasser 200 caractères")
    private String adresse;
}
