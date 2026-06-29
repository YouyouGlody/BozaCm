package com.logondigital.bozacm.DTO.mapper.reservation;

import com.logondigital.bozacm.DTO.reservation.bus.ReservationBusRequestDTO;
import com.logondigital.bozacm.DTO.reservation.bus.ReservationBusResponseDTO;
import com.logondigital.bozacm.DTO.reservation.bus.ReservationBusUpdateDTO;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.entities.reservation.ReservationBus;
import com.logondigital.bozacm.enums.StatutReservation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper pour convertir entre les entités ReservationBus et les DTOs.
 * Les infos de voyage (ville, date, prix) viennent de l'OFFRE.
 */
@Component
public class ReservationBusMapper {

    // ==================== REQUEST DTO → ENTITY ====================

    /**
     * Convertit un ReservationBusRequestDTO en entité ReservationBus.
     * @param requestDTO Le DTO contenant les données de la requête
     * @param client Le client associé à la réservation
     * @param offre L'offre réservée (fournit trajet, prix, date)
     */
    public ReservationBus toEntity(ReservationBusRequestDTO requestDTO, Client client, Offre offre) {
        if (requestDTO == null) {
            return null;
        }

        ReservationBus reservation = new ReservationBus();

        // Lien vers l'offre (source des infos de voyage)
        reservation.setOffre(offre);
        reservation.setStatutReservation(StatutReservation.EN_ATTENTE);
        reservation.setClient(client);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

        // Champs spécifiques au bus
        reservation.setCompagnieBus(requestDTO.getCompagnieBus());
        reservation.setTypeBus(requestDTO.getTypeBus());
        reservation.setClimatisation(requestDTO.getClimatisation());

        return reservation;
    }

    // ==================== ENTITY → RESPONSE DTO ====================

    public ReservationBusResponseDTO toResponseDTO(ReservationBus entity) {
        if (entity == null) {
            return null;
        }

        Client client = entity.getClient();
        Offre offre = entity.getOffre();

        ReservationBusResponseDTO dto = new ReservationBusResponseDTO();

        dto.setIdReservation(entity.getIdReservation());

        // Champs communs lus depuis l'offre
        if (offre != null) {
            dto.setVilleDeDepart(offre.getTrajet().getDepart());
            dto.setVilleArrivee(offre.getTrajet().getArrivee());
            dto.setDateDepart(offre.getDateDepart() != null ? offre.getDateDepart().atStartOfDay() : null);
            dto.setPrixReservation(offre.getPrix());
        }
        dto.setStatutReservation(entity.getStatutReservation());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (client != null) {
            dto.setClientId(client.getIdClient());
            dto.setClientNomComplet(client.getPrenom() + " " + client.getNom());
            dto.setClientEmail(client.getEmail());
            dto.setClientTelephone(client.getNumeroTelephone());
        }

        dto.setCompagnieBus(entity.getCompagnieBus());
        dto.setTypeBus(entity.getTypeBus());
        dto.setClimatisation(entity.getClimatisation());

        if (entity.getBillet() != null) {
            dto.setBilletId(entity.getBillet().getIdBillet());
            dto.setBilletNumero(entity.getBillet().getNumeroBillet());
            dto.setBilletQrcodeUrl(entity.getBillet().getQrcodeUrl());
        }

        return dto;
    }

    // ==================== UPDATE DTO → ENTITY ====================

    /**
     * Applique les modifications d'un UpdateDTO.
     * On ne modifie plus ville/date/prix (ça vient de l'offre), seulement le transport et le statut.
     */
    public void updateEntityFromDTO(ReservationBus entity, ReservationBusUpdateDTO updateDTO) {
        if (entity == null || updateDTO == null) {
            return;
        }

        if (updateDTO.getStatutReservation() != null) {
            entity.setStatutReservation(updateDTO.getStatutReservation());
        }
        if (updateDTO.getCompagnieBus() != null) {
            entity.setCompagnieBus(updateDTO.getCompagnieBus());
        }
        if (updateDTO.getTypeBus() != null) {
            entity.setTypeBus(updateDTO.getTypeBus());
        }
        if (updateDTO.getClimatisation() != null) {
            entity.setClimatisation(updateDTO.getClimatisation());
        }

        entity.setUpdatedAt(LocalDateTime.now());
    }

    // ==================== MÉTHODES UTILITAIRES ====================

    public java.util.List<ReservationBusResponseDTO> toResponseDTOList(java.util.List<ReservationBus> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}