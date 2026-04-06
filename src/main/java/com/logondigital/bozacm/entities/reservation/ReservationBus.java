package com.logondigital.bozacm.entities.reservation;

import com.logondigital.bozacm.enums.transport.TypeBus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité représentant une réservation de bus.
 * Hérite de la classe abstraite Reservation et ajoute des champs spécifiques aux bus.
 */
@Entity
@Table(name = "reservation_bus")
@DiscriminatorValue("BUS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationBus extends Reservation {

    /**
     * Nom de la compagnie de bus.
     * Exemples : "Touristique Express", "General Express", "Garanti Express"
     */
    @NotNull(message = "Le nom de la compagnie de bus est obligatoire")
    @NotBlank(message = "Le nom de la compagnie ne doit pas être vide")
    @Column(name = "compagnie_bus")
    private String compagnieBus;


    /**
     * Type de bus réservé.
     * Valeurs possibles : STANDARD, VIP
     */
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type de bus est obligatoire")
    @Column(name = "type_bus")
    private TypeBus typeBus;

    /**
     * Indique si le bus dispose de la climatisation.
     */
    @NotNull(message = "L'information sur la climatisation est obligatoire")
    @Column(name = "climatisation")
    private Boolean climatisation;

    /**
     * Retourne le type de transport pour cette réservation.
     *
     * @return "BUS"
     */
    @Override
    public String getTypeTransport() {
        return "BUS";
    }
}
