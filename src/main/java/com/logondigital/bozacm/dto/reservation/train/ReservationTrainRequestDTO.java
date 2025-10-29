package com.logondigital.bozacm.dto.reservation.train;

import com.logondigital.bozacm.enums.transport.ClasseTrain;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO pour créer une nouvelle réservation de train.
 * Utilisé lors de la requête POST /api/reservations/train
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationTrainRequestDTO {

    // ==================== CHAMPS COMMUNS (hérités de Reservation) ====================

    /**
     * Ville de départ du voyage
     */
    @NotNull(message = "La ville de départ est obligatoire")
    @NotBlank(message = "La ville de départ ne doit pas être vide")
    @Size(min = 2, max = 100, message = "La ville de départ doit contenir entre 2 et 100 caractères")
    private String villeDeDepart;

    /**
     * Ville d'arrivée du voyage
     */
    @NotNull(message = "La ville d'arrivée est obligatoire")
    @NotBlank(message = "La ville d'arrivée ne doit pas être vide")
    @Size(min = 2, max = 100, message = "La ville d'arrivée doit contenir entre 2 et 100 caractères")
    private String villeArrivee;

    /**
     * Date et heure de départ du voyage.
     * Doit être dans le futur.
     */
    @Future(message = "La date de départ doit être dans le futur")
    @NotNull(message = "La date de départ est obligatoire")
    private LocalDateTime dateDepart;

    /**
     * Prix de la réservation en FCFA
     */
    @NotNull(message = "Le prix de la réservation est obligatoire")
    @Positive(message = "Le prix doit être positif")
    @DecimalMin(value = "500.0", message = "Le prix minimum est de 500 FCFA")
    private Double prixReservation;

    /**
     * ID du client qui effectue la réservation
     */
    @NotNull(message = "L'ID du client est obligatoire")
    @Positive(message = "L'ID du client doit être positif")
    private Integer clientId;

    // ==================== CHAMPS SPÉCIFIQUES AU TRAIN ====================

    /**
     * Nom de la compagnie de train
     * Exemples : "CAM-RAIL", "SNCF", "Eurostar"
     */
    @NotNull(message = "Le nom de la compagnie de train est obligatoire")
    @NotBlank(message = "Le nom de la compagnie ne doit pas être vide")
    @Size(min = 2, max = 100, message = "Le nom de la compagnie doit contenir entre 2 et 100 caractères")
    private String compagnieTrain;

    /**
     * Numéro ou lettre du wagon
     * Exemples : "A", "B", "C", "1", "2"
     */
    @NotNull(message = "Le numéro de wagon est obligatoire")
    @NotBlank(message = "Le numéro de wagon ne doit pas être vide")
    @Size(min = 1, max = 10, message = "Le numéro de wagon doit contenir entre 1 et 10 caractères")
    private String numeroWagon;


    /**
     * Classe de voyage dans le train
     * Valeurs possibles : PREMIERE, SECONDE
     */
    @NotNull(message = "La classe de train est obligatoire")
    private ClasseTrain classeTrain;

}