package com.logondigital.bozacm.DTO.reservation.avion;

import com.logondigital.bozacm.enums.transport.ClasseAvion;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO pour créer une nouvelle réservation d'avion.
 * Le client choisit une OFFRE (qui fixe trajet, prix, date) + ses infos de transport.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationAvionRequestDTO {

    @NotNull(message = "L'ID de l'offre est obligatoire")
    @Positive(message = "L'ID de l'offre doit être positif")
    private Integer offreId;

    @NotNull(message = "L'ID du client est obligatoire")
    @Positive(message = "L'ID du client doit être positif")
    private Integer clientId;

    // ==================== CHAMPS SPÉCIFIQUES À L'AVION ====================

    @NotNull(message = "Le nom de la compagnie aérienne est obligatoire")
    @NotBlank(message = "Le nom de la compagnie ne doit pas être vide")
    @Size(min = 2, max = 100, message = "Le nom de la compagnie doit contenir entre 2 et 100 caractères")
    private String compagnieAerienne;

    @NotNull(message = "Le numéro de vol est obligatoire")
    @NotBlank(message = "Le numéro de vol ne doit pas être vide")
    @Pattern(regexp = "^[A-Z]{2}\\d{3,4}$", message = "Le numéro de vol doit être au format XX1234 (ex: AF1234)")
    private String numeroVol;

    @NotNull(message = "La classe de vol est obligatoire")
    private ClasseAvion classeAvion;

    @NotNull(message = "Le poids maximum des bagages est obligatoire")
    @Positive(message = "Le poids maximum doit être positif")
    @Min(value = 10, message = "Le poids maximum des bagages doit être au minimum 10 kg")
    @Max(value = 100, message = "Le poids maximum des bagages doit être au maximum 100 kg")
    private Integer poidsMaxBagages;

    @Size(max = 20, message = "Le numéro du terminal doit contenir au maximum 20 caractères")
    private String numeroTerminal;
}