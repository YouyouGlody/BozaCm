package com.logondigital.bozacm.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logondigital.bozacm.entities.reservation.Reservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[0-9]{9}$", message = "Le numéro doit contenir exactement 9 chiffres")
    @Column(unique = true)
    private String numeroTelephone;

    @NotNull(message = "L'email est obligatoire")
    @NotBlank(message = "L'email ne doit pas être vide")
    @Email(message = "L'email est invalide !")
    @Column(unique = true) // l'email en unique pour éviter qu'un même email soit utilisé deux fois
    private String email;

    @NotNull(message = "L'adresse est obligatoire")
    @NotBlank(message = "L'adresse ne doit pas être vide")
    private String adresse;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 🔧 Callbacks JPA pour gérer automatiquement les dates
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Association avec les autres entités

    // Un client peut avoir plusieurs réservations (HISTORIQUE).
    @JsonIgnore //pour éviter les références circulaires(sérialisation) lors de la conversion des objets Java en JSON.
    @OneToMany(mappedBy = "client", cascade = CascadeType.MERGE)
    private List<Reservation> reservations;  // ← L'HISTORIQUE !

    // Un client peut avoir plusieurs billets
    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.MERGE)
    private List<Billet> billets;



}
