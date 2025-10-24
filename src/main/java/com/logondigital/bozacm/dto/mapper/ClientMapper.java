package com.logondigital.bozacm.dto.mapper;

import com.logondigital.bozacm.dto.client.*;
import com.logondigital.bozacm.entities.Client;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre Client (Entity) et ClientDTO.

 * Responsabilités :
 * - toEntity() : ClientRequestDTO → Client (création)
 * - updateEntityFromDTO() : ClientUpdateDTO → Client (mise à jour partielle)
 * - toResponseDTO() : Client → ClientResponseDTO (réponse)
 */
@Component  // Bean Spring, injecté automatiquement
public class ClientMapper {

    // ════════════════════════════════════════════════════════════════════════
    // CRÉATION : ClientRequestDTO → Client
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Convertit un ClientRequestDTO en entité Client.

     * Utilisé dans : POST /api/v1/clients

     * Ce qui est copié : nom, prenom, email, numeroTelephone, adresse
     * Ce qui est généré : idClient (BDD), createdAt (@PrePersist)
     *
     * @param dto les données fournies par le frontend
     * @return une entité Client prête à être sauvegardée
     */
    public Client toEntity(ClientRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Client client = new Client();
        client.setNom(dto.getNom());
        client.setPrenom(dto.getPrenom());
        client.setEmail(dto.getEmail());
        client.setNumeroTelephone(dto.getNumeroTelephone());
        client.setAdresse(dto.getAdresse());

        return client;
    }

    // ════════════════════════════════════════════════════════════════════════
    // MISE À JOUR : ClientUpdateDTO → Client (update partiel)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Met à jour un Client existant avec les données du DTO.

     * Utilisé dans : PUT /api/v1/clients/{id}

     * UPDATE PARTIEL : Seuls les champs NON-NULL du DTO sont appliqués.

     * Exemple :
     * - DTO : { "email": "new@test.com" } (nom, prenom null)
     * - Résultat : Seul l'email change, nom/prenom conservés
     *
     * @param client l'entité existante à mettre à jour
     * @param dto les nouvelles données (champs null = pas de changement)
     */
    public void updateEntityFromDTO(Client client, ClientUpdateDTO dto) {
        if (dto == null || client == null) {
            return;
        }

        // Mise à jour CONDITIONNELLE (seulement si non-null)
        if (dto.getNom() != null) {
            client.setNom(dto.getNom());
        }
        if (dto.getPrenom() != null) {
            client.setPrenom(dto.getPrenom());
        }
        if (dto.getEmail() != null) {
            client.setEmail(dto.getEmail());
        }
        if (dto.getNumeroTelephone() != null) {
            client.setNumeroTelephone(dto.getNumeroTelephone());
        }
        if (dto.getAdresse() != null) {
            client.setAdresse(dto.getAdresse());
        }

        // idClient, createdAt, updatedAt : NE SONT PAS MODIFIÉS
        // updatedAt sera mis à jour automatiquement par @PreUpdate
    }

    // ════════════════════════════════════════════════════════════════════════
    // RÉPONSE : Client → ClientResponseDTO
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Convertit une entité Client en ClientResponseDTO.

     * Utilisé dans :
     * - GET /api/v1/clients (liste)
     * - GET /api/v1/clients/{id} (détail)
     * - POST /api/v1/clients (après création)
     * - PUT /api/v1/clients/{id} (après mise à jour)

     * Copie TOUS les champs (y compris ID et dates).
     *
     * @param client l'entité à convertir
     * @return le DTO de réponse
     */
    public ClientResponseDTO toResponseDTO(Client client) {
        if (client == null) {
            return null;
        }

        ClientResponseDTO dto = new ClientResponseDTO();
        dto.setIdClient(client.getIdClient());
        dto.setNom(client.getNom());
        dto.setPrenom(client.getPrenom());
        dto.setEmail(client.getEmail());
        dto.setNumeroTelephone(client.getNumeroTelephone());
        dto.setAdresse(client.getAdresse());
        dto.setCreatedAt(client.getCreatedAt());
        dto.setUpdatedAt(client.getUpdatedAt());

        return dto;
    }
}