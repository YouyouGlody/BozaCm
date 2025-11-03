package com.logondigital.bozacm.dto.mapper.reservation;

import com.logondigital.bozacm.dto.reservation.avion.ReservationAvionRequestDTO;
import com.logondigital.bozacm.dto.reservation.avion.ReservationAvionResponseDTO;
import com.logondigital.bozacm.dto.reservation.avion.ReservationAvionUpdateDTO;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.entities.reservation.ReservationAvion;
import com.logondigital.bozacm.entities.reservation.ReservationBus;
import com.logondigital.bozacm.enums.StatutReservation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper pour convertir entre les entités ReservationAvion et les DTOs.

 * Responsabilités :
 * - Convertir RequestDTO → Entity (pour la création)
 * - Convertir Entity → ResponseDTO (pour la lecture)
 * - Appliquer UpdateDTO → Entity (pour la modification)
 */
@Component
public class ReservationAvionMapper {

    // ==================== REQUEST DTO → ENTITY ====================

    /**
     * Convertit un ReservationAvionRequestDTO en entité ReservationAvion.
     * Utilisé lors de la création d'une nouvelle réservation (POST).
     *
     * @param requestDTO Le DTO contenant les données de la requête
     * @param client Le client associé à la réservation
     * @return Une nouvelle entité ReservationAvion prête à être sauvegardée
     */
    public ReservationAvion toEntity(ReservationAvionRequestDTO requestDTO, Client client) {
        if (requestDTO == null) {
            return null;
        }

        ReservationAvion reservation = new ReservationAvion();


                // Champs communs (hérités de Reservation)
        reservation.setVilleDeDepart(requestDTO.getVilleDeDepart());
        reservation.setVilleArrivee(requestDTO.getVilleArrivee());
        reservation.setDateDepart(requestDTO.getDateDepart());
        reservation.setPrixReservation(requestDTO.getPrixReservation());
        reservation.setStatutReservation(StatutReservation.EN_ATTENTE);  // Statut initial
        reservation.setClient(client);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

                // Champs spécifiques à l'avion
               reservation.setCompagnieAerienne(requestDTO.getCompagnieAerienne());
        reservation.setNumeroVol(requestDTO.getNumeroVol());
                reservation.setClasseAvion(requestDTO.getClasseAvion());
                reservation.setPoidsMaxBagages(requestDTO.getPoidsMaxBagages());
                reservation.setNumeroTerminal(requestDTO.getNumeroTerminal());

                return reservation;
    }

    // ==================== ENTITY → RESPONSE DTO ====================

    /**
     * Convertit une entité ReservationAvion en ReservationAvionResponseDTO.
     * Utilisé lors de la récupération d'une réservation (GET).
     *
     * @param entity L'entité ReservationAvion
     * @return Le DTO de réponse enrichi avec les informations client
     */
    public ReservationAvionResponseDTO toResponseDTO(ReservationAvion entity) {
        if (entity == null) {
            return null;
        }

        Client client = entity.getClient();

        return ReservationAvionResponseDTO.builder()
                // Identifiant
                .idReservation(entity.getIdReservation())

                // Champs communs
                .villeDeDepart(entity.getVilleDeDepart())
                .villeArrivee(entity.getVilleArrivee())
                .dateDepart(entity.getDateDepart())
                .prixReservation(entity.getPrixReservation())
                .statutReservation(entity.getStatutReservation())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())

                // Informations client enrichies
                .clientId(client != null ? client.getIdClient() : null)
                .clientNomComplet(client != null ? client.getPrenom() + " " + client.getNom() : null)
                .clientEmail(client != null ? client.getEmail() : null)
                .clientTelephone(client != null ? client.getNumeroTelephone() : null)

                // Champs spécifiques à l'avion
                .compagnieAerienne(entity.getCompagnieAerienne())
                .numeroVol(entity.getNumeroVol())
                .classeAvion(entity.getClasseAvion())
                .poidsMaxBagages(entity.getPoidsMaxBagages())
                .numeroTerminal(entity.getNumeroTerminal())

                // Informations billet (si présent)
                .billetId(entity.getBillet() != null ? entity.getBillet().getIdBillet() : null)
                .billetNumero(entity.getBillet() != null ? entity.getBillet().getNumeroBillet() : null)
                .billetQrcodeUrl(entity.getBillet() != null ? entity.getBillet().getQrcodeUrl() : null)
                .build();
    }

    // ==================== UPDATE DTO → ENTITY ====================

    /**
     * Applique les modifications d'un UpdateDTO sur une entité existante.
     * Utilisé lors de la modification d'une réservation (PUT/PATCH).
     *
     * Note : Seuls les champs NON-NULL du DTO sont appliqués.
     *
     * @param entity L'entité existante à modifier
     * @param updateDTO Le DTO contenant les modifications
     */
    public void updateEntityFromDTO(ReservationAvion entity, ReservationAvionUpdateDTO updateDTO) {
        if (entity == null || updateDTO == null) {
            return;
        }

        // Mettre à jour uniquement les champs fournis (non-null)

        // Champs communs
        if (updateDTO.getVilleDeDepart() != null) {
            entity.setVilleDeDepart(updateDTO.getVilleDeDepart());
        }

        if (updateDTO.getVilleArrivee() != null) {
            entity.setVilleArrivee(updateDTO.getVilleArrivee());
        }

        if (updateDTO.getDateDepart() != null) {
            entity.setDateDepart(updateDTO.getDateDepart());
        }

        if (updateDTO.getPrixReservation() != null) {
            entity.setPrixReservation(updateDTO.getPrixReservation());
        }

        if (updateDTO.getStatutReservation() != null) {
            entity.setStatutReservation(updateDTO.getStatutReservation());
        }

        // Champs spécifiques à l'avion
        if (updateDTO.getCompagnieAerienne() != null) {
            entity.setCompagnieAerienne(updateDTO.getCompagnieAerienne());
        }

        if (updateDTO.getNumeroVol() != null) {
            entity.setNumeroVol(updateDTO.getNumeroVol());
        }
        

        if (updateDTO.getClasseAvion() != null) {
            entity.setClasseAvion(updateDTO.getClasseAvion());
        }

        if (updateDTO.getPoidsMaxBagages() != null) {
            entity.setPoidsMaxBagages(updateDTO.getPoidsMaxBagages());
        }

        if (updateDTO.getNumeroTerminal() != null) {
            entity.setNumeroTerminal(updateDTO.getNumeroTerminal());
        }

        // Mettre à jour la date de modification
        entity.setUpdatedAt(LocalDateTime.now());
    }

    // ==================== MÉTHODES UTILITAIRES ====================

    /**
     * Convertit une liste d'entités en liste de DTOs de réponse.
     * Utile pour les endpoints qui retournent plusieurs réservations.
     *
     * @param entities Liste d'entités ReservationAvion
     * @return Liste de ReservationAvionResponseDTO
     */
    public java.util.List<ReservationAvionResponseDTO> toResponseDTOList(java.util.List<ReservationAvion> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}