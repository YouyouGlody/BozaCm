package com.logondigital.bozacm.dto.reservation.train;

import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.ClasseTrain;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO pour modifier une réservation de train existante.
 * Utilisé lors de la requête PUT /api/reservations/train/{id}

 * Note : Tous les champs sont optionnels (sauf validation si présente).
 * Seuls les champs fournis seront mis à jour.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationTrainUpdateDTO {

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
    @DecimalMin(value = "500.0", message = "Le prix minimum est de 500 FCFA")
    private Double prixReservation;

    /**
     * Nouveau statut (optionnel)
     * Valeurs : EN_ATTENTE, CONFIRMEE, ANNULEE, COMPLETEE
     */
    private StatutReservation statutReservation;

    // ==================== CHAMPS SPÉCIFIQUES AU TRAIN ====================

    /**
     * Nouvelle compagnie de train (optionnelle)
     */
    @Size(min = 2, max = 100, message = "Le nom de la compagnie doit contenir entre 2 et 100 caractères")
    private String compagnieTrain;

    /**
     * Nouveau numéro de wagon (optionnel)
     */
    @Size(min = 1, max = 10, message = "Le numéro de wagon doit contenir entre 1 et 10 caractères")
    private String numeroWagon;


    /**
     * Nouvelle classe de train (optionnelle)
     */
    private ClasseTrain classeTrain;



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
                || compagnieTrain != null
                || numeroWagon != null
                || classeTrain != null;
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
        if (compagnieTrain != null) count++;
        if (numeroWagon != null) count++;
        if (classeTrain != null) count++;
        return count;
    }
}