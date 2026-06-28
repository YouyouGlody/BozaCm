package com.logondigital.bozacm.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OffreRequestDTO {

    @NotBlank(message = "Le titre de l'offre est obligatoire")
    private String titre;

    @NotBlank(message = "La description de l'offre est obligatoire")
    private String description;

    @NotNull(message = "Le prix de l'offre est obligatoire")
    @Min(value = 1, message = "Le prix doit être supérieur à 0")
    private Double prix;

    @NotNull(message = "La date de départ est obligatoire")
    private LocalDate dateDepart;

    // Gestion des places (fonctionnalité avancée)
    @NotNull(message = "Le nombre de places est obligatoire")
    @Min(value = 1, message = "L'offre doit avoir au moins 1 place")
    private Integer nombrePlaces;

    @NotNull(message = "L'identifiant de l'agence est obligatoire")
    private Integer agenceId;

    @NotNull(message = "L'identifiant du trajet est obligatoire")
    private Integer trajetId;
}