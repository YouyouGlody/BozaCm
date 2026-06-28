package com.logondigital.bozacm.entities.reservation;

import com.logondigital.bozacm.entities.Billet;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.entities.Offre;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Classe abstraite représentant une réservation de transport.
 * Contient les champs communs à tous les types de transport (Bus, Train, Avion).

 * Utilise l'héritage JPA avec stratégie JOINED :
 * - Une table "reservations" contient les champs communs
 * - Des tables spécifiques (reservation_bus, reservation_train, reservation_avion)
 *   contiennent les champs spécifiques à chaque type de transport
 */
@Entity
@Table(name = "reservations")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type_transport", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReservation;




    /**
     * Statut actuel de la réservation.
     * Valeurs possibles : EN_ATTENTE, CONFIRMEE, ANNULEE, OMPLETEE
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "statut_reservation")
    private StatutReservation statutReservation;

    /**
     * Date de création de la réservation
     */
    private LocalDateTime createdAt;

    /**
     * Date de dernière modification
     */
    private LocalDateTime updatedAt;

    // ========== Callbacks JPA ==========

    /**
     * Exécuté automatiquement avant la première sauvegarde en base.
     * Initialise createdAt et le statut par défaut.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();

        // Initialiser le statut par défaut si non défini
        if (statutReservation == null) {
            statutReservation = StatutReservation.EN_ATTENTE;
        }
    }

    /**
     * Exécuté automatiquement avant chaque mise à jour.
     * Met à jour la date de modification.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ========== Relations ==========

    /**
     * Relation Many-to-One : Plusieurs réservations peuvent appartenir à un client.
     * Permet de retrouver l'historique des réservations d'un client.
     */
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "offre_id")
    private Offre offre;

    /**
     * Relation One-to-One : Chaque réservation génère un billet unique.
     * Le billet est créé automatiquement lors de la confirmation de la réservation.
     */
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Billet billet;

    // ========== Méthodes abstraites ==========

    /**
     * Méthode abstraite pour récupérer le type de transport.
     * Chaque classe fille (Bus, Train, Avion) doit implémenter cette méthode.
     *
     * @return Le type de transport ("BUS", "TRAIN", "AVION")
     */
    public abstract String getTypeTransport();


}