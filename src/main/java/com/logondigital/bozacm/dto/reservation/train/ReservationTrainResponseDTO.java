package com.logondigital.bozacm.dto.reservation.train;

import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.ClasseTrain;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO pour retourner les informations d'une réservation de train.
 * Utilisé lors des requêtes GET /api/reservations/train/{id}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationTrainResponseDTO {

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

    // ==================== CHAMPS SPÉCIFIQUES AU TRAIN ====================

    /**
     * Nom de la compagnie de train
     */
    private String compagnieTrain;

    /**
     * Numéro ou lettre du wagon
     */
    private String numeroWagon;


    /**
     * Classe de train (PREMIERE, SECONDE)
     */
    private ClasseTrain classeTrain;

    /**
     * Label de la classe en français
     * Exemples : "Première classe", "Seconde classe"
     */
    private String classeTrainLabel;



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
     * Retourne le label de la classe en français
     */
    public String getClasseTrainLabel() {
        if (classeTrain == null) return null;

        return switch (classeTrain) {
            case PREMIERE -> "Première classe";
            case SECONDE -> "Seconde classe";
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

    /**
     * Retourne la place complète (wagon + siège)
     * Exemple : "Wagon A - Siège A12"
     */
    public String getPlaceComplete() {
        return "Wagon " + numeroWagon ;
    }
}