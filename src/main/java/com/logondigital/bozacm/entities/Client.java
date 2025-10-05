package com.logondigital.bozacm.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
    private String nom;
    private String prenom;
    private Integer numeroTelephone;
    private String email;
    private String adresse;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Association avec les autres entités

    // Un client peut avoir plusieurs réservations
    @JsonIgnore //pour éviter les références circulaires(sérialisation) lors de la conversion des objets Java en JSON.
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    // Un client peut avoir plusieurs billets
    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Billet> billets;

    // Un client peut avoir plusieurs historiques de réservation
    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<HistoriqueReservation> historiques;

}
