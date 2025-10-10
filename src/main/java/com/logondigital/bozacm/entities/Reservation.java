package com.logondigital.bozacm.entities;

import com.logondigital.bozacm.enums.StatutReservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;


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

    @NotNull(message = "La ville de départ est obligatoire")
    @NotBlank(message = "La ville de départ ne doit pas être vide")
    private String villeDeDepart;

    @NotNull(message = "La ville d'arrivée est obligatoire")
    @NotBlank(message = "La ville d'arrivée ne doit pas être vide")
    private String villeArrivee;

    @Future(message = "La date de départ doit être dans le futur")
    @NotNull(message = "La date de départ est obligatoire")
    private LocalDateTime dateDepart;

    /**
     * Statut de la réservation.
     * @Enumerated(EnumType.STRING) : Stocke le nom de l'enum en base ("EN_ATTENTE")
     *                                 plutôt que l'ordinal (0, 1, 2...) qui est fragile
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "statut_reservation")
    private StatutReservation statutReservation;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // 🔧 Callbacks JPA pour gérer automatiquement les dates
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        // Initialiser le statut par défaut si non défini
        if (statutReservation == null) {
            statutReservation = StatutReservation.EN_ATTENTE;  // ← Utilisation de l'Enum
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }



    // Associations avec les autres entités

    // Plusieurs réservations peuvent appartenir à 1 client
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    // Chaque réservation est liée à un billet unique
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Billet billet;


}
