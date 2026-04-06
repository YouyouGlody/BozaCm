package com.logondigital.bozacm.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "offres", indexes = {
        @Index(name = "idx_offre_agence", columnList = "agence_id"),
        @Index(name = "idx_offre_trajet", columnList = "trajet_id"),
        @Index(name = "idx_offre_date",   columnList = "dateDepart")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"agence", "trajet", "reservationOffres"})
public class Offre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Le titre de l'offre est obligatoire")
    private String titre;

    @NotBlank(message = "La description de l'offre est obligatoire")
    private String description;

    @NotNull(message = "Le prix de l'offre est obligatoire")
    @Min(value = 1, message = "Le prix doit être supérieur à 0")
    private Double prix;

    @NotNull(message = "La date de départ est obligatoire")
    private LocalDate dateDepart;

    // ─── Gestion des places disponibles (fonctionnalité avancée) ─────────────
    // Nombre de places total défini à la création de l'offre
    @NotNull(message = "Le nombre de places est obligatoire")
    @Min(value = 1, message = "L'offre doit avoir au moins 1 place")
    private Integer nombrePlaces;

    // Décrémenté à chaque réservation confirmée — jamais en dessous de 0
    @Column(nullable = false)
    private Integer placesDisponibles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agence_id")
    @NotNull(message = "L'offre doit être liée à une agence")
    private Agence agence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trajet_id")
    @NotNull(message = "L'offre doit être liée à un trajet")
    private Trajet trajet;

    @OneToMany(mappedBy = "offre", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReservationOffre> reservationOffres = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // À la création, placesDisponibles = nombrePlaces
    @PrePersist
    public void initPlaces() {
        this.placesDisponibles = this.nombrePlaces;
    }
}