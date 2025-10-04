package com.logondigital.bozacm.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
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
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    // Un client peut avoir plusieurs billets
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Billet> billets;

    // Un client peut avoir plusieurs historiques de réservation
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<HistoriqueReservation> historiques;

}
