package com.logondigital.bozacm.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_offres", indexes = {
        @Index(name = "idx_reservation_email",  columnList = "emailClient"),
        @Index(name = "idx_reservation_statut", columnList = "statut"),
        @Index(name = "idx_reservation_offre",  columnList = "offre_id")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "offre")
public class ReservationOffre {

    // ─── Enum statut — déclaré dans la classe ─────────────────────────────────
    // Remplace l'ancien champ String statut qui acceptait n'importe quelle valeur.
    // Seules 3 valeurs sont autorisées : EN_ATTENTE, CONFIRMEE, ANNULEE
    public enum StatutReservation {
        EN_ATTENTE,   // Réservation créée, paiement non effectué
        CONFIRMEE,    // Paiement validé, voyage confirmé
        ANNULEE       // Réservation annulée (refus, remboursement...)
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Le nom du client est obligatoire")
    private String nomClient;

    @Email(message = "L'email du client doit être valide")
    @NotBlank(message = "L'email du client est obligatoire")
    private String emailClient;

    // LocalDate remplace l'ancien Date + @Temporal — plus moderne et précis
    @NotNull(message = "La date de réservation est obligatoire")
    private LocalDate dateReservation;

    // Enum typé remplace l'ancien String statut
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le statut est obligatoire")
    private StatutReservation statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offre_id")
    @NotNull(message = "La réservation doit être liée à une offre")
    private Offre offre;

    // Gérés automatiquement par @EntityListeners — plus de setCreatedAt() manuel
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}