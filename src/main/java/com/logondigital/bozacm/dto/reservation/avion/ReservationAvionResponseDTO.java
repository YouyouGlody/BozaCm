package com.logondigital.bozacm.dto.reservation.avion;

import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.ClasseAvion;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO pour retourner les informations d'une réservation d'avion.
 * Utilisé lors des requêtes GET /api/reservations/avion/{id}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationAvionResponseDTO {

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

    // ==================== CHAMPS SPÉCIFIQUES À L'AVION ====================

    /**
     * Nom de la compagnie aérienne
     */
    private String compagnieAerienne;

    /**
     * Numéro du vol (ex: "AF1234")
     */
    private String numeroVol;


    /**
     * Classe d'avion (ECONOMIE, AFFAIRES, PREMIERE)
     */
    private ClasseAvion classeAvion;

    /**
     * Label de la classe en français
     * Exemples : "Classe économique", "Classe affaires", "Première classe"
     */
    private String classeAvionLabel;

    /**
     * Poids maximum de bagages autorisé (en kg)
     */
    private Integer poidsMaxBagages;

    /**
     * Numéro du terminal de l'aéroport
     */
    private String numeroTerminal;

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
    public String getClasseAvionLabel() {
        if (classeAvion == null) return null;

        return switch (classeAvion) {
            case ECONOMIE -> "Classe économique";
            case AFFAIRES -> "Classe affaires";
            case PREMIERE -> "Première classe";
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
     * Retourne les informations du vol formatées
     * Exemple : "AF1234 - Douala → Paris"
     */
    public String getInfoVol() {
        return numeroVol + " - " + villeDeDepart + " → " + villeArrivee;
    }

    /**
     * Retourne les informations de la place complètes
     * Exemple : Classe économique - Terminal 2E"
     */
    public String getInfoPlace() {
        String info = " - " + getClasseAvionLabel();
        if (numeroTerminal != null) {
            info += " - Terminal " + numeroTerminal;
        }
        return info;
    }
}