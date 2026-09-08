package com.logondigital.bozacm.DTO.reservation.avion;

import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.ClasseAvion;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO pour modifier une réservation d'avion existante.
 * Utilisé lors de la requête PUT /api/reservations/avion/{id}

 * Note : Tous les champs sont optionnels (sauf validation si présente).
 * Seuls les champs fournis seront mis à jour.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationAvionUpdateDTO {

    // ==================== CHAMPS COMMUNS ====================

    /**
     * Nouvelle ville de départ (optionnelle)
     */
    @Size(min = 2, max = 100, message = "La ville de départ doit contenir entre 2 et 100 caractères")
    private String villeDeDepart;

    /**
     * Nouvelle ville d'arrivée (optionnelle)
     */
    @Size(min = 2, max = 100, message = "La ville d'arrivée doit contenir entre 2 et 100 caractères")
    private String villeArrivee;

    /**
     * Nouvelle date et heure de départ (optionnelle)
     * Doit être dans le futur si fourni
     */
    @Future(message = "La date de départ doit être dans le futur")
    private LocalDateTime dateDepart;

    /**
     * Nouveau prix (optionnel)
     */
    @Positive(message = "Le prix doit être positif")
    @DecimalMin(value = "10000.0", message = "Le prix minimum pour un vol est de 10 000 FCFA")
    private Double prixReservation;

    /**
     * Nouveau statut (optionnel)
     * Valeurs : EN_ATTENTE, CONFIRMEE, ANNULEE, COMPLETEE
     */
    private StatutReservation statutReservation;

    // ==================== CHAMPS SPÉCIFIQUES À L'AVION ====================

    /**
     * Nouvelle compagnie aérienne (optionnelle)
     */
    @Size(min = 2, max = 100, message = "Le nom de la compagnie doit contenir entre 2 et 100 caractères")
    private String compagnieAerienne;

    /**
     * Nouveau numéro de vol (optionnel)
     * Format : XX1234 (ex: AF1234)
     */
    @Pattern(regexp = "^[A-Z]{2}\\d{3,4}$", message = "Le numéro de vol doit être au format XX1234 (ex: AF1234)")
    private String numeroVol;


    /**
     * Nouvelle classe d'avion (optionnelle)
     */
    private ClasseAvion classeAvion;

    /**
     * Nouveau poids maximum de bagages (optionnel)
     */
    @Positive(message = "Le poids maximum doit être positif")
    @Min(value = 10, message = "Le poids maximum des bagages doit être au minimum 10 kg")
    @Max(value = 100, message = "Le poids maximum des bagages doit être au maximum 100 kg")
    private Integer poidsMaxBagages;

    /**
     * Nouveau numéro de terminal (optionnel)
     */
    @Size(max = 20, message = "Le numéro du terminal doit contenir au maximum 20 caractères")
    private String numeroTerminal;

    // ==================== MÉTHODES UTILITAIRES ====================

    /**
     * Vérifie si au moins un champ est fourni pour la mise à jour
     */
    public boolean hasAnyField() {
        return villeDeDepart != null
                || villeArrivee != null
                || dateDepart != null
                || prixReservation != null
                || statutReservation != null
                || compagnieAerienne != null
                || numeroVol != null
                || classeAvion != null
                || poidsMaxBagages != null
                || numeroTerminal != null;
    }

    /**
     * Compte le nombre de champs fournis
     */
    public int countProvidedFields() {
        int count = 0;
        if (villeDeDepart != null) count++;
        if (villeArrivee != null) count++;
        if (dateDepart != null) count++;
        if (prixReservation != null) count++;
        if (statutReservation != null) count++;
        if (compagnieAerienne != null) count++;
        if (numeroVol != null) count++;
        if (classeAvion != null) count++;
        if (poidsMaxBagages != null) count++;
        if (numeroTerminal != null) count++;
        return count;
    }
}