package com.logondigital.bozacm.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@Setter
@NoArgsConstructor
@Getter
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
    @Setter
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @Getter
    @Setter
    @Temporal(TemporalType.DATE)
    private Date updatedAt;
    @Temporal(TemporalType.DATE)
    private LocalDate dateCreation ;
    private boolean actif ;

    // 🔹 Relation ManyToOne vers Role
    @ManyToOne
    @JoinColumn(name = "role_id") // clé étrangère dans la table utilisateurs
    private Role role;



}
