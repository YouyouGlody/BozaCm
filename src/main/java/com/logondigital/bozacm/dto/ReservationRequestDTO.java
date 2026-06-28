package com.logondigital.bozacm.DTO;

import com.logondigital.bozacm.entities.ReservationOffre.StatutReservation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO de création d'une Réservation.
 * Le statut utilise l'enum StatutReservation pour garantir
 * l'intégrité des valeurs acceptées.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDTO {

    @NotBlank(message = "Le nom du client est obligatoire")
    private String nomClient;

    @Email(message = "L'email du client doit être valide")
    @NotBlank(message = "L'email du client est obligatoire")
    private String emailClient;

    @NotNull(message = "La date de réservation est obligatoire")
    private LocalDate dateReservation;

    @NotNull(message = "Le statut est obligatoire")
    private StatutReservation statut;

    @NotNull(message = "L'identifiant de l'offre est obligatoire")
    private Integer offreId;
}