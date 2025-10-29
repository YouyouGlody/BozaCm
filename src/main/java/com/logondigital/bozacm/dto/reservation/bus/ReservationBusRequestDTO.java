package com.logondigital.bozacm.dto.reservation.bus;

import com.logondigital.bozacm.enums.transport.TypeBus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO pour créer une nouvelle réservation de bus.
 * Utilisé lors de la requête POST /api/reservations/bus
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationBusRequestDTO {

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


    // ==================== CHAMPS SPÉCIFIQUES AU BUS ====================

    /**
     * Nom de la compagnie de bus
     * Exemples : "Touristique Express", "General Express", "Garanti Express"
     */
    @NotNull(message = "Le nom de la compagnie de bus est obligatoire")
    @NotBlank(message = "Le nom de la compagnie ne doit pas être vide")
    @Size(min = 2, max = 100, message = "Le nom de la compagnie doit contenir entre 2 et 100 caractères")
    private String compagnieBus;


    /**
     * Type de bus réservé
     * Valeurs possibles : STANDARD, VIP
     */
    @NotNull(message = "Le type de bus est obligatoire")
    private TypeBus typeBus;

    /**
     * Indique si le bus dispose de la climatisation
     */
    @NotNull(message = "L'information sur la climatisation est obligatoire")
    private Boolean climatisation;
}
