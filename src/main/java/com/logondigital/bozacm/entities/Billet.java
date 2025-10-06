package com.logondigital.bozacm.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

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

    @NotNull(message = "Ce champ ne doit pas etre vide")
    @NotBlank(message = "Ce champ ne doit pas contenir juste l'espace")
    private String nomClient;

    @NotNull(message = "Ce champ ne doit pas etre vide")
    @NotBlank(message = "Ce champ ne doit pas contenir juste l'espace")
    private String prenomClient;

    private String numeroBillet;
    private  String qrcodeUrl;
    private LocalDateTime dateEmission;
    private LocalDateTime dateExpiration;
    private String statutBillet;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
