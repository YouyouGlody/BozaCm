package com.logondigital.bozacm.DTO.client;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateDTO {


    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String nom;

    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    private String prenom;


    @Pattern(
            regexp = "^6[0-9]{8}$",
            message = "Le numéro doit commencer par 6 et contenir 9 chiffres"
    )
    private String numeroTelephone;

    @Size(max = 200, message = "L'adresse ne doit pas dépasser 200 caractères")
    private String adresse;
}