package com.logondigital.bozacm.entities;

import jakarta.persistence.*;
import lombok.*;

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
    private String nomClient;
    private String prenomClient;
    private Integer numeroTelephoneClient;
    private String emailClient;
    private String adresseClient;
    private Date createdAt;
    private Date updatedAt;

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
