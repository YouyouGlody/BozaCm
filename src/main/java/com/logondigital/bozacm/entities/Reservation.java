package com.logondigital.bozacm.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReservation;
    private LocalDateTime dateReservation;
    private String statutReservation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Association avec les autres entités

    // Plusieurs réservations peuvent appartenir à 1 client
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    // Chaque réservation est liée à un billet unique
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Billet billet;

    // Plusieurs réservations peuvent appartenir à un historique.
    @ManyToOne
    @JoinColumn(name = "historique_id")
    private HistoriqueReservation historiqueReservation;
}
