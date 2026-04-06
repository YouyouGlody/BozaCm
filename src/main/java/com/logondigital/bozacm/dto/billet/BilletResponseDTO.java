package com.logondigital.bozacm.dto.billet;

import com.logondigital.bozacm.enums.StatutBillet;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.ClasseAvion;
import com.logondigital.bozacm.enums.transport.ClasseTrain;
import com.logondigital.bozacm.enums.transport.TypeBus;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO pour retourner les informations complètes d'un billet.

 * Contient :
 * - Toutes les informations du billet (numéro, QR code, dates, statut)
 * - Snapshot du client sur le billet (nom/prénom figés pour l'embarquement)
 * - Informations actuelles du client
 * - Informations de la réservation associée
 * - Informations spécifiques du transport (Bus/Train/Avion) via polymorphisme

 * Utilisé dans : GET /api/v1/billets/{id}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BilletResponseDTO {

    // ==================== IDENTIFIANT ====================

    /**
     * ID unique du billet
     */
    private Integer idBillet;

    // ==================== INFORMATIONS BILLET ====================

    /**
     * Numéro unique du billet (format: BZC-UUID)
     * Exemple : BZC-a1b2c3d4-e5f6-7890-abcd-ef1234567890
     */
    private String numeroBillet;

    /**
     * URL ou chemin du QRCode généré pour ce billet
     * Utilisé pour la validation à l'embarquement
     */
    private String qrcodeUrl;

    /**
     * Date et heure d'émission du billet
     */
    private LocalDateTime dateEmission;

    /**
     * Date et heure d'expiration du billet
     * Par défaut : 24h après la date de départ du voyage
     */
    private LocalDateTime dateExpiration;

    /**
     * Statut actuel du billet
     * Valeurs : VALIDE, UTILISE, EXPIRE, ANNULE
     */
    private StatutBillet statutBillet;

    /**
     * Label du statut en français
     * Exemples : "Valide", "Utilisé", "Expiré", "Annulé"
     */
    private String statutBilletLabel;

    // ==================== SNAPSHOT CLIENT (sur le billet) ====================

    /**
     * Nom du client tel qu'inscrit sur le billet
     * SNAPSHOT : Ne change pas même si le client modifie son profil
     */
    private String nomClientSurBillet;

    /**
     * Prénom du client tel qu'inscrit sur le billet
     * SNAPSHOT : Ne change pas même si le client modifie son profil
     */
    private String prenomClientSurBillet;

    /**
     * Nom complet sur le billet (format: "Prénom NOM")
     */
    private String nomCompletSurBillet;

    // ==================== INFORMATIONS CLIENT (actuelles) ====================

    /**
     * ID du client propriétaire du billet
     */
    private Integer clientId;

    /**
     * Nom complet actuel du client (format: "Prénom NOM")
     * Peut différer du nom sur le billet si le client a modifié son profil
     */
    private String clientNomComplet;

    /**
     * Email actuel du client
     */
    private String clientEmail;

    /**
     * Téléphone actuel du client
     */
    private String clientTelephone;

    // ==================== INFORMATIONS RÉSERVATION ====================

    /**
     * ID de la réservation associée
     */
    private Integer reservationId;

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
     * Type de transport
     * Valeurs : "BUS", "TRAIN", "AVION"
     */
    private String typeTransport;

    /**
     * Statut de la réservation
     * Valeurs : EN_ATTENTE, CONFIRMEE, ANNULEE, COMPLETEE
     */
    private StatutReservation statutReservation;

    /**
     * Prix de la réservation en FCFA
     */
    private Double prixReservation;

    // ==================== INFORMATIONS SPÉCIFIQUES BUS ====================

    /**
     * Nom de la compagnie de bus (si transport = BUS)
     * Exemples : "Touristique Express", "General Express"
     */
    private String compagnieBus;

    /**
     * Type de bus (si transport = BUS)
     * Valeurs : STANDARD, VIP
     */
    private TypeBus typeBus;

    /**
     * Bus climatisé ? (si transport = BUS)
     */
    private Boolean climatisation;

    // ==================== INFORMATIONS SPÉCIFIQUES TRAIN ====================

    /**
     * Nom de la compagnie de train (si transport = TRAIN)
     * Exemples : "CAMRAIL"
     */
    private String compagnieTrain;

    /**
     * Numéro du wagon (si transport = TRAIN)
     * Exemples : "A", "B", "1", "2"
     */
    private String numeroWagon;

    /**
     * Classe de train (si transport = TRAIN)
     * Valeurs : PREMIERE, SECONDE
     */
    private ClasseTrain classeTrain;

    // ==================== INFORMATIONS SPÉCIFIQUES AVION ====================

    /**
     * Nom de la compagnie aérienne (si transport = AVION)
     * Exemples : "Air France", "Camair-Co", "Ethiopian Airlines"
     */
    private String compagnieAerienne;

    /**
     * Numéro du vol (si transport = AVION)
     * Exemples : "AF1234", "KL890"
     */
    private String numeroVol;

    /**
     * Classe d'avion (si transport = AVION)
     * Valeurs : ECONOMIE, AFFAIRES, PREMIERE
     */
    private ClasseAvion classeAvion;

    /**
     * Poids maximum de bagages en kg (si transport = AVION)
     */
    private Integer poidsMaxBagages;

    /**
     * Numéro du terminal (si transport = AVION)
     * Exemples : "2E", "Terminal Sud"
     */
    private String numeroTerminal;

    // ==================== AUDIT ====================

    /**
     * Date de création du billet
     */
    private LocalDateTime createdAt;

    /**
     * Date de dernière modification
     */
    private LocalDateTime updatedAt;

    // ==================== MÉTHODES UTILITAIRES ====================

    /**
     * Retourne le label du statut en français
     */
    public String getStatutBilletLabel() {
        if (statutBillet == null) return null;

        return switch (statutBillet) {
            case VALIDE -> "Valide";
            case UTILISE -> "Utilisé";
            case EXPIRE -> "Expiré";
            case ANNULE -> "Annulé";
        };
    }

    /**
     * Vérifie si le billet est valide
     */
    public boolean isValide() {
        return statutBillet == StatutBillet.VALIDE && !isExpireParDate();
    }

    /**
     * Vérifie si le billet est expiré par sa date
     */
    public boolean isExpireParDate() {
        return dateExpiration != null && LocalDateTime.now().isAfter(dateExpiration);
    }

    /**
     * Vérifie si le billet a été utilisé
     */
    public boolean isUtilise() {
        return statutBillet == StatutBillet.UTILISE;
    }

    /**
     * Vérifie si le billet est annulé
     */
    public boolean isAnnule() {
        return statutBillet == StatutBillet.ANNULE;
    }

    /**
     * Retourne le nom complet sur le billet
     */
    public String getNomCompletSurBillet() {
        if (prenomClientSurBillet == null || nomClientSurBillet == null) {
            return null;
        }
        return prenomClientSurBillet + " " + nomClientSurBillet;
    }

    /**
     * Retourne le trajet complet (format: "Ville Départ → Ville Arrivée")
     */
    public String getTrajetComplet() {
        if (villeDeDepart == null || villeArrivee == null) {
            return null;
        }
        return villeDeDepart + " → " + villeArrivee;
    }

    /**
     * Vérifie si le nom sur le billet correspond au nom actuel du client
     */
    public boolean nomClientInchange() {
        String nomBillet = getNomCompletSurBillet();
        return nomBillet != null && nomBillet.equalsIgnoreCase(clientNomComplet);
    }

    /**
     * Retourne le nom de la compagnie de transport (peu importe le type)
     */
    public String getCompagnieTransport() {
        if (compagnieBus != null) return compagnieBus;
        if (compagnieTrain != null) return compagnieTrain;
        if (compagnieAerienne != null) return compagnieAerienne;
        return null;
    }
}