package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.entities.Reservation;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.ReservationRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implémentation du service ReservationService.

 * Cette classe contient toute la logique métier liée à la gestion des réservations :

 *     CRUD complet (création, lecture, mise à jour, suppression)
 *     Historique des réservations (passées, à venir, par statut...)
 *     Actions spécifiques (confirmer ou annuler une réservation)

 * Elle utilise le design pattern "Service Layer" de Spring :
 * le contrôleur appelle ce service, qui lui-même communique avec la couche Repository (DAO).
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    // === Déclaration du Repository ===
    private final ReservationRepo reservationRepo;


    // Injection de dépendance via le constructeur
    /**
     * Constructeur avec injection de dépendance (par Spring).
     * Permet d’injecter automatiquement une instance de ReservationRepo.

     * @param reservationRepo Repository de gestion des réservations (accès BDD)
     */
    public ReservationServiceImpl(ReservationRepo reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    // ===========================================================
    // ==========        CRUD DE BASE DES RÉSERVATIONS       =====
    // ===========================================================


    // 1. Ajouter une nouvelle réservation
    /**
     * Crée une nouvelle réservation et la sauvegarde en base de données.
     * La date de création est automatiquement gérée via l'annotation @PrePersist dans l'entité.
     *
     * @param reservation objet Reservation à enregistrer
     * @return la réservation sauvegardée
     */
    @Override
    public Reservation createReservation(Reservation reservation) {
        // Pas besoin d’appeler setCreatedAt() → @PrePersist le fait automatiquement
        return this.reservationRepo.save(reservation);
    }


    // 2. Récupérer une réservation par son ID
    /**
     * Récupère une réservation par son identifiant.
     *
     * @param idReservation identifiant unique de la réservation
     * @return la réservation trouvée
     * @throws RessourceNotFoundException si aucune réservation n’existe avec cet ID
     */
    @Override
    public Reservation getReservationById(Integer idReservation) {
        return this.reservationRepo.findById(idReservation).orElseThrow(
                () -> new RessourceNotFoundException("Réservation non trouvée avec l'ID: " + idReservation)
        );
    }

    // 3. Récupérer toutes les réservations
    /**
     * Récupère toutes les réservations existantes dans la base de données.
     *
     * @return liste complète des réservations
     */
    @Override
    public List<Reservation> getAllReservations() {
        return this.reservationRepo.findAll();
    }

    // 4. Mettre à jour les informations de la réservation
    /**
     * Met à jour une réservation existante (par son ID).
     * La date de mise à jour est gérée automatiquement via l'annotation @PreUpdate.

     * @param idReservation ID de la réservation à modifier
     * @param reservation   nouvelle version de la réservation
     * @return la réservation mise à jour
     */
    @Override
    public Reservation updateReservation(Integer idReservation, Reservation reservation) {
        // On cherche d’abord la réservation existante
        Reservation reservationToUpdate = this.reservationRepo.findById(idReservation).orElseThrow(
                () -> new RessourceNotFoundException("Réservation non trouvée avec l'ID: " + idReservation
        ));

        // On met à jour uniquement les champs nécessaires
        reservationToUpdate.setDateDepart(reservation.getDateDepart());
        reservationToUpdate.setStatutReservation(reservation.getStatutReservation());

        // Pas besoin d’appeler setUpdatedAt() → @PreUpdate s’en charge

        return this.reservationRepo.saveAndFlush(reservationToUpdate);
    }


    // 5. Supprimer Reservation par son ID
    /**
     * Supprime une réservation par son identifiant.
     *
     * @param idReservation ID de la réservation à supprimer
     */
    @Override
    public void deleteReservation(Integer idReservation) {
        this.reservationRepo.deleteById(idReservation);

    }

    // 5. Supprimer toutes les Reservations
    /**
     * Supprime toutes les réservations de la base de données.
     */
    @Override
    public void deleteAllReservations() {
        this.reservationRepo.deleteAll();
    }


    // ===========================================================
    // ==========        MÉTHODES MÉTIER (HISTORIQUES)      =====
    // ===========================================================


    /**
     * Récupère la liste complète des réservations d’un client,
     * triées par ordre décroissant (de la plus récente à la plus ancienne).

     *Desc : Descending (Plus récent à la plus ancienne)

     * @param clientId identifiant du client
     * @return liste des réservations triées
     */
    @Override
    public List<Reservation> getHistoriqueComplet(Integer clientId) {
        return reservationRepo.findByClientIdClientOrderByCreatedAtDesc(clientId);
    }

    /**
     * Récupère l’historique des réservations déjà passées (date antérieure à maintenant).

     * @param clientId identifiant du client
     * @return liste des réservations passées
     */
    @Override
    public List<Reservation> getHistoriquePasse(Integer clientId) {
        LocalDateTime maintenant = LocalDateTime.now();
        return reservationRepo.findReservationsPassees(clientId, maintenant);
    }

    /**
     * Récupère les réservations à venir (date future par rapport à maintenant).

     * @param clientId identifiant du client
     * @return liste des réservations à venir
     */
    @Override
    public List<Reservation> getReservationsAVenir(Integer clientId) {
           LocalDateTime maintenant = LocalDateTime.now();
        return reservationRepo.findReservationsAVenir(clientId, maintenant);
    }

    /**
     * Récupère les réservations d’un client selon leur statut (ex : EN_ATTENTE, CONFIRMEE...).

     * @param clientId identifiant du client
     * @param statut   statut recherché
     * @return liste des réservations correspondant au statut
     */
    @Override
    public List<Reservation> getReservationsParStatut(Integer clientId, StatutReservation statut) {
        return reservationRepo.findByClientIdClientAndStatutReservation(clientId, statut);
    }


    /**
     * Récupère les réservations selon un trajet spécifique (ville de départ et ville d’arrivée).

     * @param villeDepart  ville de départ
     * @param villeArrivee ville d’arrivée
     * @return liste des réservations correspondant à ce trajet
     */
    @Override
    public List<Reservation> getReservationsParTrajet(String villeDepart, String villeArrivee) {
      return reservationRepo.findByVilleDeDepartAndVilleArrivee(villeDepart, villeArrivee);
    }


    /**
     * Compte le nombre total de réservations d’un client.
     *
     * @param clientId identifiant du client
     * @return nombre total de réservations
     */
    @Override
    public long countReservationsByClient(Integer clientId) {
        return reservationRepo.countByClientIdClient(clientId);
    }


    /**
     * Confirme une réservation existante (change son statut en CONFIRMEE).
     *
     * @param idReservation ID de la réservation à confirmer
     * @return réservation mise à jour avec statut CONFIRMEE
     */
    @Override
    public Reservation confirmerReservation(Integer idReservation) {
        Reservation reservation = getReservationById(idReservation);
        reservation.setStatutReservation(StatutReservation.CONFIRMEE);
        return reservationRepo.save(reservation);
    }


    /**
     * Annule une réservation existante (change son statut en ANNULEE).
     *
     * @param idReservation ID de la réservation à annuler
     * @return réservation mise à jour avec statut ANNULEE
     */
    @Override
    public Reservation annulerReservation(Integer idReservation) {
        Reservation reservation = getReservationById(idReservation);
        reservation.setStatutReservation(StatutReservation.ANNULEE);
        return reservationRepo.save(reservation);
    }
}
