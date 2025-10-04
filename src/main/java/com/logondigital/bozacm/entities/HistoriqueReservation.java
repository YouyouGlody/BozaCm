package com.logondigital.bozacm.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


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
    private String statutHistorique;
    private LocalDateTime creationDate;


    // Association avec les autres entités

    // Plusieurs historiques appartiennent à un client.
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    // Un historique peut concerner plusieurs réservations
    @OneToMany(mappedBy = "historiqueReservation", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

}
