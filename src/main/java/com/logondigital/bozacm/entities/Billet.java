package com.logondigital.bozacm.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "billets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Billet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBillet;
    private Integer idClient;
    private Integer idReservation;
    private String nomClient;
    private String prenomClient;
    private String numeroBillet;
    private  String qrcodeUrl;
    private LocalDateTime dateEmission;
    private LocalDateTime dateExpiration;
    private String statutBillet;
    private Date createdAt;
    private Date updatedAt;

    // Association avec les autres entités

    // Des billets sont associés à un client.
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    // Un billet correspond à une réservation.
    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

}
