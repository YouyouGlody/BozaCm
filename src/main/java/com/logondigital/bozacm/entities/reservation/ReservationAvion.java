package com.logondigital.bozacm.entities.reservation;

import com.logondigital.bozacm.enums.transport.ClasseAvion;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Entité représentant une réservation d'avion.
 * Hérite de la classe abstraite Reservation et ajoute des champs spécifiques aux vols.
 */
@Entity
@Table(name = "reservation_avion")
@DiscriminatorValue("AVION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationAvion extends Reservation {

    /**
     * Nom de la compagnie aérienne.
     * Exemples : "Air France", "Camair-Co", "Ethiopian Airlines"
     */
    @NotNull(message = "Le nom de la compagnie aérienne est obligatoire")
    @NotBlank(message = "Le nom de la compagnie ne doit pas être vide")
    @Column(name = "compagnie_aerienne")
    private String compagnieAerienne;

    /**
     * Numéro du vol.
     * Format : Code compagnie (2 lettres) + Numéro (3-4 chiffres)
     * Exemples : "AF1234", "KL890", "ET502"
     */
    @NotNull(message = "Le numéro de vol est obligatoire")
    @NotBlank(message = "Le numéro de vol ne doit pas être vide")
    @Pattern(regexp = "^[A-Z]{2}\\d{3,4}$", message = "Le numéro de vol doit être au format XX1234 (ex: AF1234)")
    @Column(name = "numero_vol")
    private String numeroVol;
    

    /**
     * Classe de voyage dans l'avion.
     * Valeurs possibles : ECONOMIE, AFFAIRES, PREMIERE
     */
    @Enumerated(EnumType.STRING)
    @NotNull(message = "La classe de vol est obligatoire")
    @Column(name = "classe_avion")
    private ClasseAvion classeAvion;

    /**
     * Poids maximum de bagages autorisé en kilogrammes.
     * Varie selon la classe et la compagnie (généralement 20-30kg en économie, 40-50kg en affaires/première)
     */
    @NotNull(message = "Le poids maximum des bagages est obligatoire")
    @Positive(message = "Le poids maximum doit être positif")
    @Column(name = "poids_max_bagages")
    private Integer poidsMaxBagages;

    /**
     * Numéro du terminal de l'aéroport.
     * Exemples : "2E", "1", "3", "Terminal Sud"
     */
    @Column(name = "numero_terminal")
    private String numeroTerminal;

    /**
     * Retourne le type de transport pour cette réservation.
     *
     * @return "AVION"
     */
    @Override
    public String getTypeTransport() {
        return "AVION";
    }
}