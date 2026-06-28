package com.logondigital.bozacm.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OffreResponseDTO {

    private Integer          id;
    private String           titre;
    private String           description;
    private Double           prix;
    private LocalDate        dateDepart;
    private Integer          nombrePlaces;
    private Integer          placesDisponibles;  // visible dans Swagger en temps réel
    private AgenceResponseDTO agence;
    private TrajetResponseDTO trajet;
}