package com.logondigital.bozacm.dto.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO pour la réponse contenant les données d'un client.

 * Représente ce que l'API retourne au frontend après :
 *      - Création d'un client (POST /api/v1/clients)
 *      - Récupération d'un client (GET /api/v1/clients/{id})
 *      - Mise à jour d'un client (PUT /api/v1/clients/{id})
 *      - Recherche par email ou téléphone

 * Inclut tous les champs visibles pour le client, y compris
 * les champs auto-générés (ID, dates de création/modification).

 * Exemple de JSON généré :
 * {
 *   "idClient": 1,
 *   "nom": "Dupont",
 *   "prenom": "Jean",
 *   "email": "jean@example.com",
 *   "numeroTelephone": "612345678",
 *   "adresse": "Yaoundé, Cameroun",
 *   "createdAt": "13/10/2025 14:30:00",
 *   "updatedAt": "14/10/2025 10:15:00"
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDTO {

    /**
     * ID unique du client (généré automatiquement par la base de données).
     * Présent uniquement dans les réponses, jamais dans les requêtes.
     */
    private Integer idClient;

    /**
     * Nom du client.
     */
    private String nom;

    /**
     * Prénom du client.
     */
    private String prenom;

    /**
     * Adresse email du client.
     * Unique dans le système.
     */
    private String email;

    /**
     * Numéro de téléphone du client (format camerounais).
     * Format : 6XXXXXXXX (9 chiffres commençant par 6)
     */
    private String numeroTelephone;

    /**
     * Adresse complète du client.
     */
    private String adresse;

    /**
     * Date et heure de création du compte client.
     * Générée automatiquement lors de l'inscription.
     * Format d'affichage : dd/MM/yyyy HH:mm:ss
     */
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * Date et heure de la dernière modification du profil.
     * Mise à jour automatiquement à chaque modification.
     * Null si le profil n'a jamais été modifié.
     * Format d'affichage : dd/MM/yyyy HH:mm:ss
     */
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
}
