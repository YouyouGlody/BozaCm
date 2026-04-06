package com.logondigital.bozacm.entities.reservation;

import com.logondigital.bozacm.enums.transport.ClasseTrain;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Entité représentant une réservation de train.
 * Hérite de la classe abstraite Reservation et ajoute des champs spécifiques aux trains.
 */
@Entity
@Table(name = "reservation_train")
@DiscriminatorValue("TRAIN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationTrain extends Reservation {

    /**
     * Nom de la compagnie de train.
     * Exemples : "CAMRAIL", "SNCF", "Eurostar"
     */
    @NotNull(message = "Le nom de la compagnie de train est obligatoire")
    @NotBlank(message = "Le nom de la compagnie ne doit pas être vide")
    @Column(name = "compagnie_train")
    private String compagnieTrain;

    /**
     * Numéro ou lettre du wagon.
     * Exemples : "A", "B", "C", "1", "2"
     */
    @NotNull(message = "Le numéro de wagon est obligatoire")
    @NotBlank(message = "Le numéro de wagon ne doit pas être vide")
    @Column(name = "numero_wagon")
    private String numeroWagon;


    /**
     * Classe de voyage dans le train.
     * Valeurs possibles : PREMIERE, SECONDE
     */
    @Enumerated(EnumType.STRING)
    @NotNull(message = "La classe de train est obligatoire")
    @Column(name = "classe_train")
    private ClasseTrain classeTrain;



    /**
     * Retourne le type de transport pour cette réservation.
     *
     * @return "TRAIN"
     */
    @Override
    public String getTypeTransport() {
        return "TRAIN";
    }
}