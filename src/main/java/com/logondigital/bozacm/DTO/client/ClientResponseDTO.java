package com.logondigital.bozacm.DTO.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.logondigital.bozacm.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDTO {


    private Integer idClient;
    private String nom;
    private String prenom;
    private String email;
    private String numeroTelephone;
    private String adresse;
    private String photoUrl;
    private boolean cniComplete;    // true si recto ET verso présents
    private boolean passeportPresent;
    private Role role;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
}
