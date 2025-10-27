package com.logondigital.bozacm.dto.mapper;

import com.logondigital.bozacm.dto.reservation.*;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.entities.Reservation;
import com.logondigital.bozacm.enums.StatutReservation;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre Reservation (Entity) et ReservationDTO.
 */
@Component
public class ReservationMapper {

    // ════════════════════════════════════════════════════════════════════════
    // CRÉATION : ReservationRequestDTO → Reservation
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Convertit un ReservationRequestDTO en entité Reservation.

     * Le client doit être chargé séparément par le service.
     * Le statut est automatiquement défini à EN_ATTENTE.
     *
     * @param dto les données de la réservation
     * @param client l'entité Client (récupérée par le service)
     * @return une entité Reservation
     */
    public Reservation toEntity(ReservationRequestDTO dto, Client client) {
        if (dto == null) {
            return null;
        }

        Reservation reservation = new Reservation();
        reservation.setVilleDeDepart(dto.getVilleDeDepart());
        reservation.setVilleArrivee(dto.getVilleArrivee());
        reservation.setDateDepart(dto.getDateDepart());
        reservation.setClient(client);
        reservation.setStatutReservation(StatutReservation.EN_ATTENTE);

        return reservation;
    }

    // ════════════════════════════════════════════════════════════════════════
    // MISE À JOUR : ReservationUpdateDTO → Reservation
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Met à jour une Reservation existante avec les données du DTO.
     * Seuls les champs non-null sont mis à jour (update partiel).
     *
     * @param reservation l'entité existante
     * @param dto les nouvelles données
     */
    public void updateEntityFromDTO(Reservation reservation, ReservationUpdateDTO dto) {
        if (dto == null || reservation == null) {
            return;
        }

        if (dto.getVilleDeDepart() != null) {
            reservation.setVilleDeDepart(dto.getVilleDeDepart());
        }
        if (dto.getVilleArrivee() != null) {
            reservation.setVilleArrivee(dto.getVilleArrivee());
        }
        if (dto.getDateDepart() != null) {
            reservation.setDateDepart(dto.getDateDepart());
        }
        if (dto.getStatutReservation() != null) {
            reservation.setStatutReservation(dto.getStatutReservation());
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // RÉPONSE : Reservation → ReservationResponseDTO
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Convertit une entité Reservation en ReservationResponseDTO.
     * Inclut les informations essentielles du client.
     *
     * @param reservation l'entité à convertir
     * @return le DTO de réponse
     */
    public ReservationResponseDTO toResponseDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setIdReservation(reservation.getIdReservation());
        dto.setVilleDeDepart(reservation.getVilleDeDepart());
        dto.setVilleArrivee(reservation.getVilleArrivee());
        dto.setDateDepart(reservation.getDateDepart());
        dto.setStatutReservation(reservation.getStatutReservation());
        dto.setCreatedAt(reservation.getCreatedAt());
        dto.setUpdatedAt(reservation.getUpdatedAt());

        // Ajouter les infos du client si disponible
        if (reservation.getClient() != null) {
            dto.setClientId(reservation.getClient().getIdClient());
            dto.setNomClient(reservation.getClient().getNom());
            dto.setPrenomClient(reservation.getClient().getPrenom());
            dto.setEmailClient(reservation.getClient().getEmail());
        }

        return dto;
    }
}