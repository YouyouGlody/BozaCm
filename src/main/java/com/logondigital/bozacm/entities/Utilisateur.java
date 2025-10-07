package com.logondigital.bozacm.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Setter
@NoArgsConstructor
@Getterr
@Entity
@Table(name = "utilisateurs")
public class Utilisateur {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
     private Integer utilisateurId;
    private String nom ;
    private String prenom ;
    private String email ;
    private String motDePasse ;
    private String telephone ;
    private String photoProfil ;
    private LocalDate dateCreation ;
    private boolean actif ;



}
