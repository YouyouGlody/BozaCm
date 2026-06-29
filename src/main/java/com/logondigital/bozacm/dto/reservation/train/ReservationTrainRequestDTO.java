package com.logondigital.bozacm.DTO.reservation.train;

import com.logondigital.bozacm.enums.transport.ClasseTrain;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO pour créer une nouvelle réservation de train.
 * Le client choisit une OFFRE (qui fixe trajet, prix, date) + ses infos de transport.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationTrainRequestDTO {

    @NotNull(message = "L'ID de l'offre est obligatoire")
    @Positive(message = "L'ID de l'offre doit être positif")
    private Integer offreId;

    @NotNull(message = "L'ID du client est obligatoire")
    @Positive(message = "L'ID du client doit être positif")
    private Integer clientId;

    // ==================== CHAMPS SPÉCIFIQUES AU TRAIN ====================

    @NotNull(message = "Le nom de la compagnie de train est obligatoire")
    @NotBlank(message = "Le nom de la compagnie ne doit pas être vide")
    @Size(min = 2, max = 100, message = "Le nom de la compagnie doit contenir entre 2 et 100 caractères")
    private String compagnieTrain;

    @NotNull(message = "Le numéro de wagon est obligatoire")
    @NotBlank(message = "Le numéro de wagon ne doit pas être vide")
    @Size(min = 1, max = 10, message = "Le numéro de wagon doit contenir entre 1 et 10 caractères")
    private String numeroWagon;

    @NotNull(message = "La classe de train est obligatoire")
    private ClasseTrain classeTrain;
}