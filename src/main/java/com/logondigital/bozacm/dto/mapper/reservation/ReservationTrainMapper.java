package com.logondigital.bozacm.dto.mapper.reservation;

import com.logondigital.bozacm.dto.reservation.train.ReservationTrainRequestDTO;
import com.logondigital.bozacm.dto.reservation.train.ReservationTrainResponseDTO;
import com.logondigital.bozacm.dto.reservation.train.ReservationTrainUpdateDTO;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.entities.reservation.ReservationTrain;
import com.logondigital.bozacm.enums.StatutReservation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper pour convertir entre les entités ReservationTrain et les DTOs.

 * Responsabilités :
 * - Convertir RequestDTO → Entity (pour la création)
 * - Convertir Entity → ResponseDTO (pour la lecture)
 * - Appliquer UpdateDTO → Entity (pour la modification)
 */
@Component
public class ReservationTrainMapper {

    // ==================== REQUEST DTO → ENTITY ====================

    /**
     * Convertit un ReservationTrainRequestDTO en entité ReservationTrain.
     * Utilisé lors de la création d'une nouvelle réservation (POST).
     *
     * @param requestDTO Le DTO contenant les données de la requête
     * @param client Le client associé à la réservation
     * @return Une nouvelle entité ReservationTrain prête à être sauvegardée.
     */
    public ReservationTrain toEntity(ReservationTrainRequestDTO requestDTO, Client client) {
        if (requestDTO == null) {
            return null;
        }

        ReservationTrain reservation = new ReservationTrain();


                // Champs communs (hérités de Reservation)
        reservation.setVilleDeDepart(requestDTO.getVilleDeDepart());
        reservation.setVilleArrivee(requestDTO.getVilleArrivee());
        reservation.setDateDepart(requestDTO.getDateDepart());
        reservation.setPrixReservation(requestDTO.getPrixReservation());
        reservation.setStatutReservation(StatutReservation.EN_ATTENTE);  // Statut initial
        reservation.setClient(client);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

                // Champs spécifiques au train
                reservation.setCompagnieTrain(requestDTO.getCompagnieTrain());
               reservation.setNumeroWagon(requestDTO.getNumeroWagon());
               reservation.setClasseTrain(requestDTO.getClasseTrain());
              return reservation;
    }

    // ==================== ENTITY → RESPONSE DTO ====================

    /**
     * Convertit une entité ReservationTrain en ReservationTrainResponseDTO.
     * Utilisé lors de la récupération d'une réservation (GET).
     *
     * @param entity L'entité ReservationTrain
     * @return Le DTO de réponse enrichi avec les informations client
     */
    public ReservationTrainResponseDTO toResponseDTO(ReservationTrain entity) {
        if (entity == null) {
            return null;
        }

        Client client = entity.getClient();

        return ReservationTrainResponseDTO.builder()
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

                // Champs spécifiques au train
                .compagnieTrain(entity.getCompagnieTrain())
                .numeroWagon(entity.getNumeroWagon())
                .classeTrain(entity.getClasseTrain())

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

     * Note : Seuls les champs NON-NULL du DTO sont appliqués.
     *
     * @param entity L'entité existante à modifier
     * @param updateDTO Le DTO contenant les modifications
     */
    public void updateEntityFromDTO(ReservationTrain entity, ReservationTrainUpdateDTO updateDTO) {
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

        // Champs spécifiques au train
        if (updateDTO.getCompagnieTrain() != null) {
            entity.setCompagnieTrain(updateDTO.getCompagnieTrain());
        }

        if (updateDTO.getNumeroWagon() != null) {
            entity.setNumeroWagon(updateDTO.getNumeroWagon());
        }


        if (updateDTO.getClasseTrain() != null) {
            entity.setClasseTrain(updateDTO.getClasseTrain());
        }


        // Mettre à jour la date de modification
        entity.setUpdatedAt(LocalDateTime.now());
    }

    // ==================== MÉTHODES UTILITAIRES ====================

    /**
     * Convertit une liste d'entités en liste de DTOs de réponse.
     * Utile pour les endpoints qui retournent plusieurs réservations.
     *
     * @param entities Liste d'entités ReservationTrain
     * @return Liste de ReservationTrainResponseDTO
     */
    public java.util.List<ReservationTrainResponseDTO> toResponseDTOList(java.util.List<ReservationTrain> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}