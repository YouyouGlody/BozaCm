package com.logondigital.bozacm.entities;

import com.logondigital.bozacm.entities.reservation.Reservation;
import com.logondigital.bozacm.enums.StatutBillet;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


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

    @NotNull(message = "Le numéro de billet est obligatoire")
    @NotBlank(message = "Le numéro de billet ne doit pas être vide")
    @Column(unique = true, nullable = false)
    private String numeroBillet;

    private String qrcodeUrl;

    @NotNull(message = "La date d'émission est obligatoire")
    private LocalDateTime dateEmission;

    private LocalDateTime dateExpiration;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_billet", nullable = false)
    private StatutBillet statutBillet;

    @NotNull(message = "Le nom sur le billet est obligatoire")
    @NotBlank(message = "Le nom sur le billet ne doit pas être vide")
    private String nomClientSurBillet;

    @NotNull(message = "Le prénom sur le billet est obligatoire")
    @NotBlank(message = "Le prénom sur le billet ne doit pas être vide")
    private String prenomClientSurBillet;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Callbacks JPA pour gérer automatiquement les données
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        dateEmission = LocalDateTime.now();

        // Générer un numéro de billet unique si non défini
        if (numeroBillet == null || numeroBillet.isEmpty()) {
            numeroBillet = genererNumeroBillet();
        }

        // Définir le statut par défaut
        if (statutBillet == null) {
            statutBillet = StatutBillet.VALIDE;
        }

        // Copier les infos du client comme snapshot
        if (client != null) {
            this.nomClientSurBillet = client.getNom();
            this.prenomClientSurBillet = client.getPrenom();
        }

        // Calculer la date d'expiration (24h après le départ du voyage, lu depuis l'offre)
        if (reservation != null && reservation.getOffre() != null
                && reservation.getOffre().getDateDepart() != null) {
            this.dateExpiration = reservation.getOffre().getDateDepart().atStartOfDay().plusHours(24);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    private String genererNumeroBillet() {
        return "BZC-" + UUID.randomUUID();
    }

    // Association avec les autres entités

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false, unique = true)
    private Reservation reservation;
}