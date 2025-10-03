package com.logondigital.bozacm.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "historique_reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class HistoriqueReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistoriqueReservation;
    private Integer idClient;
    private Integer idBillet;
    private Integer idNumeroBillet;
    private String nomClient;
    private String prenomClient;
    private String statutReservation;
    private LocalDateTime dateReservation;
    private Date creationDate;


    // Association avec les autres entités

    // Plusieurs historiques appartiennent à un client.
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    // Un historique peut concerner plusieurs réservations
    @OneToMany(mappedBy = "historiqueReservation", cascade = CascadeType.ALL)
    private Set<Reservation> reservations;

}
