package com.logondigital.bozacm.entities;

import jakarta.persistence.*;

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

    private String numeroBillet;

    private  String qrcodeUrl;

    private LocalDateTime dateEmission;

    private LocalDateTime dateExpiration;

    private String statutBillet;


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

    // Des billets sont associés à un client.
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    // Un billet correspond à une réservation.
    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

}
