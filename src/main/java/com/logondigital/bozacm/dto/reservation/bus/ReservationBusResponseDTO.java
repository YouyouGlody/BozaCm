package com.logondigital.bozacm.DTO.reservation.bus;

import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.TypeBus;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO pour retourner les informations d'une réservation de bus.
 * Utilisé lors des requêtes GET /api/reservations/bus/{id}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationBusResponseDTO {

    // ==================== IDENTIFIANT ====================

    /**
     * ID unique de la réservation
     */
    private Integer idReservation;

    // ==================== CHAMPS COMMUNS ====================

    /**
     * Ville de départ du voyage
     */
    private String villeDeDepart;

    /**
     * Ville d'arrivée du voyage
     */
    private String villeArrivee;

    /**
     * Date et heure de départ du voyage
     */
    private LocalDateTime dateDepart;

    /**
     * Prix de la réservation en FCFA
     */
    private Double prixReservation;

    /**
     * Statut actuel de la réservation
     * Valeurs : EN_ATTENTE, CONFIRMEE, ANNULEE, COMPLETEE
     */
    private StatutReservation statutReservation;

    /**
     * Date de création de la réservation
     */
    private LocalDateTime createdAt;

    /**
     * Date de dernière modification
     */
    private LocalDateTime updatedAt;

    // ==================== INFORMATIONS CLIENT ====================

    /**
     * ID du client
     */
    private Integer clientId;

    /**
     * Nom complet du client (format: "Prénom NOM")
     */
    private String clientNomComplet;

    /**
     * Email du client
     */
    private String clientEmail;

    /**
     * Téléphone du client
     */
    private String clientTelephone;

    // ==================== CHAMPS SPÉCIFIQUES AU BUS ====================

    /**
     * Nom de la compagnie de bus
     */
    private String compagnieBus;


    /**
     * Type de bus (STANDARD, VIP)
     */
    private TypeBus typeBus;

    /**
     * Label du type de bus en français
     * Exemples : "Standard", "VIP"
     */
    private String typeBusLabel;

    /**
     * Indique si le bus dispose de la climatisation
     */
    private Boolean climatisation;

    // ==================== INFORMATIONS BILLET (si généré) ====================

    /**
     * ID du billet associé (null si pas encore généré)
     */
    private Integer billetId;

    /**
     * Numéro du billet (format: BZC-UUID)
     */
    private String billetNumero;

    /**
     * URL du QR Code du billet
     */
    private String billetQrcodeUrl;

    // ==================== MÉTHODES UTILITAIRES ====================

    /**
     * Retourne le label du type de bus en français
     */
    public String getTypeBusLabel() {
        if (typeBus == null) return null;

        return switch (typeBus) {
            case STANDARD -> "Standard";
            case VIP -> "VIP";
        };
    }

    /**
     * Vérifie si la réservation est confirmée
     */
    public boolean isConfirmee() {
        return statutReservation == StatutReservation.CONFIRMEE;
    }

    /**
     * Vérifie si un billet a été généré
     */
    public boolean hasBillet() {
        return billetId != null;
    }
}