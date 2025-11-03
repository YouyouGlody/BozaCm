package com.logondigital.bozacm.dto.reservation.avion;

import com.logondigital.bozacm.enums.transport.ClasseAvion;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO pour créer une nouvelle réservation d'avion.
 * Utilisé lors de la requête POST /api/reservations/avion
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationAvionRequestDTO {

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
    @DecimalMin(value = "10000.0", message = "Le prix minimum pour un vol est de 10 000 FCFA")
    private Double prixReservation;

    /**
     * ID du client qui effectue la réservation
     */
    @NotNull(message = "L'ID du client est obligatoire")
    @Positive(message = "L'ID du client doit être positif")
    private Integer clientId;

    // ==================== CHAMPS SPÉCIFIQUES À L'AVION ====================

    /**
     * Nom de la compagnie aérienne
     * Exemples : "Air France", "Camair-Co", "Ethiopian Airlines"
     */
    @NotNull(message = "Le nom de la compagnie aérienne est obligatoire")
    @NotBlank(message = "Le nom de la compagnie ne doit pas être vide")
    @Size(min = 2, max = 100, message = "Le nom de la compagnie doit contenir entre 2 et 100 caractères")
    private String compagnieAerienne;

    /**
     * Numéro du vol
     * Format : Code compagnie (2 lettres) + Numéro (3-4 chiffres)
     * Exemples : "AF1234", "KL890", "ET502"
     */
    @NotNull(message = "Le numéro de vol est obligatoire")
    @NotBlank(message = "Le numéro de vol ne doit pas être vide")
    @Pattern(regexp = "^[A-Z]{2}\\d{3,4}$", message = "Le numéro de vol doit être au format XX1234 (ex: AF1234)")
    private String numeroVol;


    /**
     * Classe de voyage dans l'avion
     * Valeurs possibles : ECONOMIE, AFFAIRES, PREMIERE
     */
    @NotNull(message = "La classe de vol est obligatoire")
    private ClasseAvion classeAvion;

    /**
     * Poids maximum de bagages autorisé en kilogrammes
     * Varie selon la classe (20-30kg en économie, 40-50kg en affaires/première)
     */
    @NotNull(message = "Le poids maximum des bagages est obligatoire")
    @Positive(message = "Le poids maximum doit être positif")
    @Min(value = 10, message = "Le poids maximum des bagages doit être au minimum 10 kg")
    @Max(value = 100, message = "Le poids maximum des bagages doit être au maximum 100 kg")
    private Integer poidsMaxBagages;

    /**
     * Numéro du terminal de l'aéroport (optionnel)
     * Exemples : "2E", "1", "3", "Terminal sud"
     */
    @Size(max = 20, message = "Le numéro du terminal doit contenir au maximum 20 caractères")
    private String numeroTerminal;
}