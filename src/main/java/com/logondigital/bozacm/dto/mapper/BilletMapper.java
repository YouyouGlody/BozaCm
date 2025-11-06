package com.logondigital.bozacm.dto.mapper;

import com.logondigital.bozacm.dto.billet.BilletRequestDTO;
import com.logondigital.bozacm.dto.billet.BilletResponseDTO;
import com.logondigital.bozacm.dto.billet.BilletUpdateDTO;
import com.logondigital.bozacm.entities.Billet;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.entities.reservation.Reservation;
import com.logondigital.bozacm.entities.reservation.ReservationAvion;
import com.logondigital.bozacm.entities.reservation.ReservationBus;
import com.logondigital.bozacm.entities.reservation.ReservationTrain;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Mapper pour convertir entre les entités Billet et les DTOs.

 * Responsabilités :
 * - Convertir RequestDTO → Entity (pour la création)
 * - Convertir Entity → ResponseDTO (pour la lecture)
 * - Appliquer UpdateDTO → Entity (pour la modification)
 * - Gérer le polymorphisme des réservations (Bus/Train/Avion)

 * PARTICULARITÉ :
 * Le mapper gère la relation polymorphe avec Reservation.
 * Il extrait automatiquement les informations spécifiques du type de transport.
 */
@Component
public class BilletMapper {

    // ==================== REQUEST DTO → ENTITY ====================

    /**
     * Convertit un BilletRequestDTO en entité Billet.
     * Utilisé lors de la création d'un nouveau billet (POST).

     * IMPORTANT : La plupart des champs sont auto-générés via @PrePersist :
     * - numeroBillet (BZC-UUID)
     * - dateEmission (maintenant)
     * - dateExpiration (dateDepart + 24h)
     * - statutBillet (VALIDE)
     * - nomClientSurBillet / prenomClientSurBillet (snapshot du client)
     * - createdAt

     * Le mapper associe uniquement les relations client et reservation.
     *
     * @param requestDTO Le DTO contenant les IDs
     * @param client Le client associé
     * @param reservation La réservation associée
     * @return Une nouvelle entité Billet prête à être sauvegardée
     */
    public Billet toEntity(BilletRequestDTO requestDTO, Client client, Reservation reservation) {
        if (requestDTO == null) {
            return null;
        }

        Billet billet = new Billet();

        // Associer les relations
        billet.setClient(client);
        billet.setReservation(reservation);

        // Les autres champs sont auto-générés par @PrePersist dans l'entité :
        // - numeroBillet = genererNumeroBillet()
        // - dateEmission = LocalDateTime.now()
        // - dateExpiration = reservation.getDateDepart().plusHours(24)
        // - statutBillet = StatutBillet.VALIDE
        // - nomClientSurBillet = client.getNom()
        // - prenomClientSurBillet = client.getPrenom()
        // - createdAt = LocalDateTime.now()

        return billet;
    }

    // ==================== ENTITY → RESPONSE DTO ====================

    /**
     * Convertit une entité Billet en BilletResponseDTO.
     * Utilisé lors de la récupération d'un billet (GET).

     * GESTION DU POLYMORPHISME :
     * La méthode détecte automatiquement le type de réservation (Bus/Train/Avion)
     * et extrait les informations spécifiques correspondantes.
     *
     * @param billet L'entité Billet
     * @return Le DTO de réponse enrichi avec toutes les informations
     */
    public BilletResponseDTO toResponseDTO(Billet billet) {
        if (billet == null) {
            return null;
        }

        Client client = billet.getClient();
        Reservation reservation = billet.getReservation();

        BilletResponseDTO dto = new BilletResponseDTO();

        // ========== INFORMATIONS BILLET ==========
        dto.setIdBillet(billet.getIdBillet());
        dto.setNumeroBillet(billet.getNumeroBillet());
        dto.setQrcodeUrl(billet.getQrcodeUrl());
        dto.setDateEmission(billet.getDateEmission());
        dto.setDateExpiration(billet.getDateExpiration());
        dto.setStatutBillet(billet.getStatutBillet());

        // ========== SNAPSHOT CLIENT (sur le billet) ==========
        dto.setNomClientSurBillet(billet.getNomClientSurBillet());
        dto.setPrenomClientSurBillet(billet.getPrenomClientSurBillet());

        // ========== INFORMATIONS CLIENT (actuelles) ==========
        if (client != null) {
            dto.setClientId(client.getIdClient());
            dto.setClientNomComplet(client.getPrenom() + " " + client.getNom());
            dto.setClientEmail(client.getEmail());
            dto.setClientTelephone(client.getNumeroTelephone());
        }

        // ========== INFORMATIONS RÉSERVATION ==========
        if (reservation != null) {
            dto.setReservationId(reservation.getIdReservation());
            dto.setVilleDeDepart(reservation.getVilleDeDepart());
            dto.setVilleArrivee(reservation.getVilleArrivee());
            dto.setDateDepart(reservation.getDateDepart());
            dto.setTypeTransport(reservation.getTypeTransport());
            dto.setStatutReservation(reservation.getStatutReservation());
            dto.setPrixReservation(reservation.getPrixReservation());

            // ========== POLYMORPHISME : Informations spécifiques par type ==========

            // Si c'est une réservation de BUS
            if (reservation instanceof ReservationBus reservationBus) {
                dto.setCompagnieBus(reservationBus.getCompagnieBus());
                dto.setTypeBus(reservationBus.getTypeBus());
                dto.setClimatisation(reservationBus.getClimatisation());
            }

            // Si c'est une réservation de TRAIN
            else if (reservation instanceof ReservationTrain reservationTrain) {
                dto.setCompagnieTrain(reservationTrain.getCompagnieTrain());
                dto.setNumeroWagon(reservationTrain.getNumeroWagon());
                dto.setClasseTrain(reservationTrain.getClasseTrain());
            }

            // Si c'est une réservation d'AVION
            else if (reservation instanceof ReservationAvion reservationAvion) {
                dto.setCompagnieAerienne(reservationAvion.getCompagnieAerienne());
                dto.setNumeroVol(reservationAvion.getNumeroVol());
                dto.setClasseAvion(reservationAvion.getClasseAvion());
                dto.setPoidsMaxBagages(reservationAvion.getPoidsMaxBagages());
                dto.setNumeroTerminal(reservationAvion.getNumeroTerminal());
            }
        }

        // ========== AUDIT ==========
        dto.setCreatedAt(billet.getCreatedAt());
        dto.setUpdatedAt(billet.getUpdatedAt());

        return dto;
    }

    // ==================== UPDATE DTO → ENTITY ====================

    /**
     * Applique les modifications d'un UpdateDTO sur une entité existante.
     * Utilisé lors de la modification d'un billet (PUT/PATCH).

     * CHAMPS MODIFIABLES :
     * - statutBillet (pour marquer comme UTILISE, EXPIRE, ANNULE)
     * - qrcodeUrl (si régénération du QR Code)

     * CHAMPS NON-MODIFIABLES (pour garantir l'intégrité) :
     * - numeroBillet (unique et immuable)
     * - dateEmission (historique figé)
     * - dateExpiration (calculée automatiquement)
     * - nomClientSurBillet / prenomClientSurBillet (snapshot figé)
     * - client / reservation (relations figées)

     * Note : Seuls les champs NON-NULL du DTO sont appliqués.
     *
     * @param billet L'entité existante à modifier
     * @param updateDTO Le DTO contenant les modifications
     */
    public void updateEntityFromDTO(Billet billet, BilletUpdateDTO updateDTO) {
        if (billet == null || updateDTO == null) {
            return;
        }

        // Mettre à jour uniquement les champs fournis (non-null)

        if (updateDTO.getStatutBillet() != null) {
            billet.setStatutBillet(updateDTO.getStatutBillet());
        }

        if (updateDTO.getQrcodeUrl() != null) {
            billet.setQrcodeUrl(updateDTO.getQrcodeUrl());
        }

        // Mettre à jour la date de modification
        billet.setUpdatedAt(LocalDateTime.now());
    }

    // ==================== MÉTHODES UTILITAIRES ====================

    /**
     * Convertit une liste d'entités en liste de DTOs de réponse.
     * Utile pour les endpoints qui retournent plusieurs billets.
     *
     * @param billets Liste d'entités Billet
     * @return Liste de BilletResponseDTO
     */
    public List<BilletResponseDTO> toResponseDTOList(List<Billet> billets) {
        if (billets == null) {
            return null;
        }

        return billets.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Vérifie si un billet peut être mis à jour.
     * Règle métier : Un billet UTILISE ou ANNULE ne peut plus être modifié.
     *
     * @param billet Le billet à vérifier
     * @return true si modifiable, false sinon
     */
    public boolean isModifiable(Billet billet) {
        if (billet == null || billet.getStatutBillet() == null) {
            return false;
        }

        return switch (billet.getStatutBillet()) {
            case VALIDE, EXPIRE -> true;  // Modifiable
            case UTILISE, ANNULE -> false; // Non modifiable
        };
    }

    /**
     * Vérifie si un changement de statut est autorisé.
     * Règle métier : Certains changements de statut sont interdits.

     * Transitions autorisées :
     * - VALIDE → UTILISE (après embarquement)
     * - VALIDE → EXPIRE (expiration automatique)
     * - VALIDE → ANNULE (annulation de réservation)
     * - EXPIRE → ANNULE (nettoyage)

     * Transitions interdites :
     * - UTILISE → * (un billet utilisé ne peut plus changer)
     * - ANNULE → * (un billet annulé ne peut plus changer)
     * - * → VALIDE (on ne peut pas "revalider" un billet)
     *
     * @param ancienStatut Le statut actuel
     * @param nouveauStatut Le nouveau statut souhaité
     * @return true si la transition est autorisée
     */
    public boolean isTransitionStatutAutorisee(
            com.logondigital.bozacm.enums.StatutBillet ancienStatut,
            com.logondigital.bozacm.enums.StatutBillet nouveauStatut) {

        if (ancienStatut == null || nouveauStatut == null) {
            return false;
        }

        // Si même statut, pas de changement
        if (ancienStatut == nouveauStatut) {
            return true;
        }

        return switch (ancienStatut) {
            case VALIDE -> nouveauStatut == com.logondigital.bozacm.enums.StatutBillet.UTILISE
                    || nouveauStatut == com.logondigital.bozacm.enums.StatutBillet.EXPIRE
                    || nouveauStatut == com.logondigital.bozacm.enums.StatutBillet.ANNULE;

            case EXPIRE -> nouveauStatut == com.logondigital.bozacm.enums.StatutBillet.ANNULE;

            case UTILISE, ANNULE -> false; // États finaux, aucune transition possible
        };
    }
}