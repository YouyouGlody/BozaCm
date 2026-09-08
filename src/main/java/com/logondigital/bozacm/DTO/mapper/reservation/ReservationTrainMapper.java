package com.logondigital.bozacm.DTO.mapper.reservation;

import com.logondigital.bozacm.DTO.reservation.train.ReservationTrainRequestDTO;
import com.logondigital.bozacm.DTO.reservation.train.ReservationTrainResponseDTO;
import com.logondigital.bozacm.DTO.reservation.train.ReservationTrainUpdateDTO;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.entities.reservation.ReservationTrain;
import com.logondigital.bozacm.enums.StatutReservation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper pour convertir entre les entités ReservationTrain et les DTOs.
 * Les infos de voyage (ville, date, prix) viennent de l'OFFRE.
 */
@Component
public class ReservationTrainMapper {

    // ==================== REQUEST DTO → ENTITY ====================

    public ReservationTrain toEntity(ReservationTrainRequestDTO requestDTO, Client client, Offre offre) {
        if (requestDTO == null) {
            return null;
        }

        ReservationTrain reservation = new ReservationTrain();

        reservation.setOffre(offre);
        reservation.setStatutReservation(StatutReservation.EN_ATTENTE);
        reservation.setClient(client);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

        reservation.setCompagnieTrain(requestDTO.getCompagnieTrain());
        reservation.setNumeroWagon(requestDTO.getNumeroWagon());
        reservation.setClasseTrain(requestDTO.getClasseTrain());

        return reservation;
    }

    // ==================== ENTITY → RESPONSE DTO ====================

    public ReservationTrainResponseDTO toResponseDTO(ReservationTrain entity) {
        if (entity == null) {
            return null;
        }

        Client client = entity.getClient();
        Offre offre = entity.getOffre();

        return ReservationTrainResponseDTO.builder()
                .idReservation(entity.getIdReservation())
                .villeDeDepart(offre != null ? offre.getTrajet().getDepart() : null)
                .villeArrivee(offre != null ? offre.getTrajet().getArrivee() : null)
                .dateDepart(offre != null && offre.getDateDepart() != null ? offre.getDateDepart().atStartOfDay() : null)
                .prixReservation(offre != null ? offre.getPrix() : null)
                .statutReservation(entity.getStatutReservation())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .clientId(client != null ? client.getIdClient() : null)
                .clientNomComplet(client != null ? client.getPrenom() + " " + client.getNom() : null)
                .clientEmail(client != null ? client.getEmail() : null)
                .compagnieTrain(entity.getCompagnieTrain())
                .numeroWagon(entity.getNumeroWagon())
                .classeTrain(entity.getClasseTrain())
                .billetId(entity.getBillet() != null ? entity.getBillet().getIdBillet() : null)
                .billetNumero(entity.getBillet() != null ? entity.getBillet().getNumeroBillet() : null)
                .billetQrcodeUrl(entity.getBillet() != null ? entity.getBillet().getQrcodeUrl() : null)
                .build();
    }

    // ==================== UPDATE DTO → ENTITY ====================

    public void updateEntityFromDTO(ReservationTrain entity, ReservationTrainUpdateDTO updateDTO) {
        if (entity == null || updateDTO == null) {
            return;
        }

        if (updateDTO.getStatutReservation() != null) {
            entity.setStatutReservation(updateDTO.getStatutReservation());
        }
        if (updateDTO.getCompagnieTrain() != null) {
            entity.setCompagnieTrain(updateDTO.getCompagnieTrain());
        }
        if (updateDTO.getNumeroWagon() != null) {
            entity.setNumeroWagon(updateDTO.getNumeroWagon());
        }
        if (updateDTO.getClasseTrain() != null) {
            entity.setClasseTrain(updateDTO.getClasseTrain());
        }

        entity.setUpdatedAt(LocalDateTime.now());
    }

    // ==================== MÉTHODES UTILITAIRES ====================

    public java.util.List<ReservationTrainResponseDTO> toResponseDTOList(java.util.List<ReservationTrain> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}