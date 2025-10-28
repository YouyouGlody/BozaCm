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


    /**
     * Numéro unique du billet.
     * Généré automatiquement au format : BZC-UUID
     * Exemple : BZC-a1b2c3d4-e5f6-7890-abcd-ef1234567890
     */
    @NotNull(message = "Le numéro de billet est obligatoire")
    @NotBlank(message = "Le numéro de billet ne doit pas être vide")
    @Column(unique = true, nullable = false)
    private String numeroBillet;

    /**
     * URL ou chemin du QRCode généré pour ce billet.
     * Le QR code contient le numéro de billet et permet la validation à l'embarquement.
     */
    private String qrcodeUrl;


    /**
     * Date et heure d'émission du billet.
     * Rempli automatiquement lors de la création.
     */
    @NotNull(message = "La date d'émission est obligatoire")
    private LocalDateTime dateEmission;

    /**
     * Date et heure d'expiration du billet.
     * Par défaut : 24 heures après la date de départ du voyage.
     */
    private LocalDateTime dateExpiration;

    /**
     * Statut actuel du billet.
     * Utilise l'Enum StatutBillet pour garantir des valeurs valides.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "statut_billet", nullable = false)
    private StatutBillet statutBillet;

    /**
     *  SNAPSHOT : Nom du client au moment de l'achat du billet.
     * Même si le client modifie son profil plus tard, le billet garde le nom d'origine.
     * Important pour la vérification d'identité lors de l'embarquement.
     */
    @NotNull(message = "Le nom sur le billet est obligatoire")
    @NotBlank(message = "Le nom sur le billet ne doit pas être vide")
    private String nomClientSurBillet;

    /**
     *  SNAPSHOT : Prénom du client au moment de l'achat du billet.
     */
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

        // Calculer la date d'expiration (24h après le départ du voyage)
        if (reservation != null && reservation.getDateDepart() != null) {
            this.dateExpiration = reservation.getDateDepart().plusHours(24);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    /**
     * UUID = Universally Unique IDentifier (Identifiant Unique Universel)
     * Définition :
     * Un UUID est un nombre de 128 bits (très grand !) affiché sous forme de texte.
     * Génère un numéro de billet unique au format : BZC-UUID
     * Exemple : BZC-a1b2c3d4-e5f6-7890-abcd-ef1234567890

     * @return Le numéro de billet généré
     */
    // Étape 1 : Appel de la méthode
    private String genererNumeroBillet() {

        // Étape 2 : UUID.randomUUID() génère un UUID
        // Étape 3 : .toString() convertit en String
        // Étape 4 : Concaténation avec "BZC-" // numero = "BZC-a1b2c3d4-e5f6-7890-abcd-ef1234567890"
        // Étape 5 : Retour du résultat

        return "BZC-" + UUID.randomUUID();
    }

    // Association avec les autres entités

    /**
     * Relation Many-to-One : Plusieurs billets peuvent appartenir à un client.
     * Permet de retrouver tous les billets d'un client.
     */
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    /**
     * Relation One-to-One : Un billet correspond à UNE réservation unique.
     * C'est ce côté qui possède la clé étrangère (reservation_id).

     * IMPORTANT: Reservation est maintenant une classe abstraite.
     * JPA gère automatiquement le polymorphisme :
     *       - reservation peut être une ReservationBus
     *       - reservation peut être une ReservationTrain
     *       - reservation peut être une ReservationAvion
     */
    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false, unique = true)
    private Reservation reservation;


}