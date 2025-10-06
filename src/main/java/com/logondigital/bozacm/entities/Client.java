package com.logondigital.bozacm.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

import java.util.List;

@Entity
@Table(name = "clients")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor

public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idClient;

    @NotNull(message = "Le nom est obligatoire")
    @NotBlank(message = "Le nom ne doit pas être vide")
    private String nom;

    @NotNull(message = "Le prénom est obligatoire")
    @NotBlank(message = "Le prénom ne doit pas être vide")
    private String prenom;

    @NotNull(message = "Le numéro de téléphone est obligatoire")
    private Integer numeroTelephone;

    @NotNull(message = "L'email est obligatoire")
    @NotBlank(message = "L'email ne doit pas être vide")
    @Email(message = "L'email est invalide !")
    private String email;

    @NotNull(message = "L'adresse est obligatoire")
    @NotBlank(message = "L'adresse ne doit pas être vide")
    private String adresse;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    // Association avec les autres entités

    // Un client peut avoir plusieurs réservations
    @JsonIgnore //pour éviter les références circulaires(sérialisation) lors de la conversion des objets Java en JSON.
    @OneToMany(mappedBy = "client", cascade = CascadeType.MERGE)
    private List<Reservation> reservations;

    // Un client peut avoir plusieurs billets
    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.MERGE)
    private List<Billet> billets;

    // Un client peut avoir plusieurs historiques de réservation
    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.MERGE)
    private List<HistoriqueReservation> historiques;

}
