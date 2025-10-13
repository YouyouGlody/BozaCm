package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.entities.Reservation;
import com.logondigital.bozacm.enums.StatutReservation;

import java.util.List;

/**
 *Un Service contient la logique métier de ton application.
 * C'est la couche entre le Controller (qui reçoit les requêtes) et le Repository (qui accède à la BD).

 * Service pour gérer la logique métier des réservations.
 */

public interface ReservationService {

    // ===========================================================
    // ==========        CRUD DE BASE DES RÉSERVATIONS       =====
    // ===========================================================

    // 1. Ajouter une nouvelle réservation
    Reservation createReservation(Reservation reservation);

    // 2. Récupérer une réservation par son ID
    Reservation getReservationById(Integer idReservation);

    // 3. Récupérer toutes les réservations
    List<Reservation> getAllReservations();

    // 4. Mettre à jour les informations de la réservation
    Reservation updateReservation(Integer idReservation, Reservation reservation);

    // 5. Supprimer une réservation par son ID
    void deleteReservation(Integer idReservation);





    // ===========================================================
    // ==========        MÉTHODE MÉTIER (HISTORIQUE)     =====
    // ===========================================================


    /**
     * Récupère l'historique COMPLET des réservations d'un client.
     * Trié par date de création (plus récent en premier).
     */
    List<Reservation> getHistoriqueComplet(Integer clientId);


    /**
     * Récupère les réservations PASSÉES d'un client (voyages terminés).
     */
    List<Reservation> getHistoriquePasse(Integer clientId);


    /**
     * Récupère les réservations À VENIR d'un client (voyages futurs).
     */
    List<Reservation> getReservationsAVenir(Integer clientId);


    /**
     * Récupère les réservations d'un client par statut.
     */
    List<Reservation> getReservationsParStatut(Integer clientId, StatutReservation statut);


    /**
     * Trouve les réservations pour un trajet spécifique.
     */
    List<Reservation> getReservationsParTrajet(String villeDepart, String villeArrivee);


    /**
     * Compte le nombre de réservations d'un client.
     */
    long countReservationsByClient(Integer clientId);


    /**
     * Confirme une réservation (change le statut à CONFIRMÉE).
     */
    Reservation confirmerReservation(Integer idReservation);


    /**
     * Annule une réservation (change le statut à ANNULÉE).
     */
    Reservation annulerReservation(Integer idReservation);

}
