package com.logondigital.bozacm.DTO.mapper.reservation;

import com.logondigital.bozacm.DTO.reservation.avion.ReservationAvionRequestDTO;
import com.logondigital.bozacm.DTO.reservation.avion.ReservationAvionResponseDTO;
import com.logondigital.bozacm.DTO.reservation.avion.ReservationAvionUpdateDTO;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.entities.reservation.ReservationAvion;
import com.logondigital.bozacm.enums.StatutReservation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper pour convertir entre les entités ReservationAvion et les DTOs.
 * Les infos de voyage (ville, date, prix) viennent de l'OFFRE.
 */
@Component
public class ReservationAvionMapper {

    // ==================== REQUEST DTO → ENTITY ====================

    public ReservationAvion toEntity(ReservationAvionRequestDTO requestDTO, Client client, Offre offre) {
        if (requestDTO == null) {
            return null;
        }

        ReservationAvion reservation = new ReservationAvion();

        reservation.setOffre(offre);
        reservation.setStatutReservation(StatutReservation.EN_ATTENTE);
        reservation.setClient(client);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

        reservation.setCompagnieAerienne(requestDTO.getCompagnieAerienne());
        reservation.setNumeroVol(requestDTO.getNumeroVol());
        reservation.setClasseAvion(requestDTO.getClasseAvion());
        reservation.setPoidsMaxBagages(requestDTO.getPoidsMaxBagages());
        reservation.setNumeroTerminal(requestDTO.getNumeroTerminal());

        return reservation;
    }

    // ==================== ENTITY → RESPONSE DTO ====================

    public ReservationAvionResponseDTO toResponseDTO(ReservationAvion entity) {
        if (entity == null) {
            return null;
        }

        Client client = entity.getClient();
        Offre offre = entity.getOffre();

        return ReservationAvionResponseDTO.builder()
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
                .clientTelephone(client != null ? client.getNumeroTelephone() : null)
                .compagnieAerienne(entity.getCompagnieAerienne())
                .numeroVol(entity.getNumeroVol())
                .classeAvion(entity.getClasseAvion())
                .poidsMaxBagages(entity.getPoidsMaxBagages())
                .numeroTerminal(entity.getNumeroTerminal())
                .billetId(entity.getBillet() != null ? entity.getBillet().getIdBillet() : null)
                .billetNumero(entity.getBillet() != null ? entity.getBillet().getNumeroBillet() : null)
                .billetQrcodeUrl(entity.getBillet() != null ? entity.getBillet().getQrcodeUrl() : null)
                .build();
    }

    // ==================== UPDATE DTO → ENTITY ====================

    public void updateEntityFromDTO(ReservationAvion entity, ReservationAvionUpdateDTO updateDTO) {
        if (entity == null || updateDTO == null) {
            return;
        }

        if (updateDTO.getStatutReservation() != null) {
            entity.setStatutReservation(updateDTO.getStatutReservation());
        }
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

        entity.setUpdatedAt(LocalDateTime.now());
    }

    // ==================== MÉTHODES UTILITAIRES ====================

    public java.util.List<ReservationAvionResponseDTO> toResponseDTOList(java.util.List<ReservationAvion> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}