package com.logondigital.bozacm.dto.mapper.reservation;

import com.logondigital.bozacm.dto.reservation.bus.ReservationBusRequestDTO;
import com.logondigital.bozacm.dto.reservation.bus.ReservationBusResponseDTO;
import com.logondigital.bozacm.dto.reservation.bus.ReservationBusUpdateDTO;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.entities.reservation.ReservationBus;
import com.logondigital.bozacm.enums.StatutReservation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper pour convertir entre les entités ReservationBus et les DTOs.

 * Responsabilités :
 * - Convertir RequestDTO → Entity (pour la création)
 * - Convertir Entity → ResponseDTO (pour la lecture)
 * - Appliquer UpdateDTO → Entity (pour la modification)
 */
@Component
public class ReservationBusMapper {

    // ==================== REQUEST DTO → ENTITY ====================

    /**
     * Convertit un ReservationBusRequestDTO en entité ReservationBus.
     * Utilisé lors de la création d'une nouvelle réservation (POST).
     *
     * @param requestDTO Le DTO contenant les données de la requête
     * @param client Le client associé à la réservation
     * @return Une nouvelle entité ReservationBus prête à être sauvegardée.
     */
    public ReservationBus toEntity(ReservationBusRequestDTO requestDTO, Client client) {
        if (requestDTO == null) {
            return null;
        }

        ReservationBus reservation = new ReservationBus();

        // Champs communs (hérités de Reservation)
        reservation.setVilleDeDepart(requestDTO.getVilleDeDepart());
        reservation.setVilleArrivee(requestDTO.getVilleArrivee());
        reservation.setDateDepart(requestDTO.getDateDepart());
        reservation.setPrixReservation(requestDTO.getPrixReservation());
        reservation.setStatutReservation(StatutReservation.EN_ATTENTE);  // Statut initial
        reservation.setClient(client);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

        // Note: typeTransport est géré automatiquement par JPA via @DiscriminatorValue("BUS")
        // Pas besoin de le setter manuellement !

        // Champs spécifiques au bus
        reservation.setCompagnieBus(requestDTO.getCompagnieBus());
        reservation.setTypeBus(requestDTO.getTypeBus());
        reservation.setClimatisation(requestDTO.getClimatisation());

        return reservation;
    }

    // ==================== ENTITY → RESPONSE DTO ====================

    /**
     * Convertit une entité ReservationBus en ReservationBusResponseDTO.
     * Utilisé lors de la récupération d'une réservation (GET).
     *
     * @param entity L'entité ReservationBus
     * @return Le DTO de réponse enrichi avec les informations client
     */
    public ReservationBusResponseDTO toResponseDTO(ReservationBus entity) {
        if (entity == null) {
            return null;
        }

        Client client = entity.getClient();

        ReservationBusResponseDTO dto = new ReservationBusResponseDTO();

        // Identifiant
        dto.setIdReservation(entity.getIdReservation());

        // Champs communs
        dto.setVilleDeDepart(entity.getVilleDeDepart());
        dto.setVilleArrivee(entity.getVilleArrivee());
        dto.setDateDepart(entity.getDateDepart());
        dto.setPrixReservation(entity.getPrixReservation());
        dto.setStatutReservation(entity.getStatutReservation());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // Informations client enrichies
        if (client != null) {
            dto.setClientId(client.getIdClient());
            dto.setClientNomComplet(client.getPrenom() + " " + client.getNom());
            dto.setClientEmail(client.getEmail());
            dto.setClientTelephone(client.getNumeroTelephone());
        }

        // Champs spécifiques au bus
        dto.setCompagnieBus(entity.getCompagnieBus());
        dto.setTypeBus(entity.getTypeBus());
        dto.setClimatisation(entity.getClimatisation());

        // Informations billet (si présent)
        if (entity.getBillet() != null) {
            dto.setBilletId(entity.getBillet().getIdBillet());
            dto.setBilletNumero(entity.getBillet().getNumeroBillet());
            dto.setBilletQrcodeUrl(entity.getBillet().getQrcodeUrl());
        }

        return dto;
    }

    // ==================== UPDATE DTO → ENTITY ====================

    /**
     * Applique les modifications d'un UpdateDTO sur une entité existante.
     * Utilisé lors de la modification d'une réservation (PUT/PATCH).

     * Note : Seuls les champs NON-NULL du DTO sont appliqués.
     *
     * @param entity L'entité existante à modifier
     * @param updateDTO Le DTO contenant les modifications
     */
    public void updateEntityFromDTO(ReservationBus entity, ReservationBusUpdateDTO updateDTO) {
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

        // Champs spécifiques au bus
        if (updateDTO.getCompagnieBus() != null) {
            entity.setCompagnieBus(updateDTO.getCompagnieBus());
        }

        if (updateDTO.getTypeBus() != null) {
            entity.setTypeBus(updateDTO.getTypeBus());
        }

        if (updateDTO.getClimatisation() != null) {
            entity.setClimatisation(updateDTO.getClimatisation());
        }

        // Mettre à jour la date de modification
        entity.setUpdatedAt(LocalDateTime.now());
    }

    // ==================== MÉTHODES UTILITAIRES ====================

    /**
     * Convertit une liste d'entités en liste de DTOs de réponse.
     * Utile pour les endpoints qui retournent plusieurs réservations.
     *
     * @param entities Liste d'entités ReservationBus
     * @return Liste de ReservationBusResponseDTO
     */
    public java.util.List<ReservationBusResponseDTO> toResponseDTOList(java.util.List<ReservationBus> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}