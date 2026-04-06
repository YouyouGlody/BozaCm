package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.entities.reservation.Reservation;
import com.logondigital.bozacm.enums.StatutReservation;

import java.util.List;

/**
 * Interface générique pour les services de réservation.

 * Définit le contrat commun que tous les services de réservation
 * (Bus, Train, Avion) doivent implémenter.

 * Cette interface contient :
 * - CRUD de base (Create, Read, Update, Delete)
 * - Gestion de l'historique client
 * - Filtrage et recherche
 * - Actions métier (confirmer, annuler)
 *
 * @param <T> Type de réservation (ReservationBus, ReservationTrain, ReservationAvion)
 */
public interface ReservationService<T extends Reservation> {

    // ===========================================================
    // ==========        CRUD DE BASE                       ======
    // ===========================================================

    /**
     * Crée une nouvelle réservation en base de données.
     * La date de création et le statut initial (EN_ATTENTE) sont gérés automatiquement.
     *
     * @param reservation la réservation à créer
     * @return la réservation créée avec son ID généré
     */
    T createReservation(T reservation);

    /**
     * Récupère une réservation par son identifiant.
     *
     * @param id l'identifiant unique de la réservation
     * @return la réservation trouvée
     * @throws com.logondigital.bozacm.exceptions.RessourceNotFoundException si la réservation n'existe pas
     */
    T getReservationById(Integer id);

    /**
     * Récupère toutes les réservations de ce type de transport.
     *
     * @return liste de toutes les réservations
     */
    List<T> getAllReservations();

    /**
     * Met à jour une réservation existante.
     * La date de mise à jour est gérée automatiquement via @PreUpdate.
     *
     * @param id l'identifiant de la réservation à modifier
     * @param reservation la réservation avec les nouvelles données
     * @return la réservation mise à jour
     * @throws com.logondigital.bozacm.exceptions.RessourceNotFoundException si la réservation n'existe pas
     */
    T updateReservation(Integer id, T reservation);

    /**
     * Supprime une réservation par son identifiant.
     *
     * @param id l'identifiant de la réservation à supprimer
     * @throws com.logondigital.bozacm.exceptions.RessourceNotFoundException si la réservation n'existe pas
     */
    void deleteReservation(Integer id);

    // ===========================================================
    // ==========        HISTORIQUE CLIENT                  ======
    // ===========================================================

    /**
     * Récupère l'historique COMPLET des réservations d'un client.
     * Trié par date de création décroissante (plus récent en premier).
     *
     * @param clientId l'identifiant du client
     * @return liste de toutes les réservations du client
     */
    List<T> getHistoriqueComplet(Integer clientId);

    /**
     * Récupère les réservations PASSÉES d'un client.
     * Filtre : dateDepart < maintenant
     *
     * @param clientId l'identifiant du client
     * @return liste des réservations passées
     */
    List<T> getHistoriquePasse(Integer clientId);

    /**
     * Récupère les réservations À VENIR d'un client.
     * Filtre : dateDepart >= maintenant
     * Trié par date de départ croissante (plus proche en premier).
     *
     * @param clientId l'identifiant du client
     * @return liste des réservations à venir
     */
    List<T> getReservationsAVenir(Integer clientId);

    // ===========================================================
    // ==========        FILTRAGE ET RECHERCHE              ======
    // ===========================================================

    /**
     * Récupère les réservations d'un client par statut.
     *
     * @param clientId l'identifiant du client
     * @param statut le statut recherché (EN_ATTENTE, CONFIRMEE, ANNULEE, COMPLETEE)
     * @return liste des réservations avec ce statut
     */
    List<T> getReservationsParStatut(Integer clientId, StatutReservation statut);

    /**
     * Recherche les réservations pour un trajet spécifique.
     *
     * @param villeDepart la ville de départ
     * @param villeArrivee la ville d'arrivée
     * @return liste des réservations pour ce trajet
     */
    List<T> getReservationsParTrajet(String villeDepart, String villeArrivee);

    /**
     * Compte le nombre total de réservations d'un client.
     *
     * @param clientId l'identifiant du client
     * @return le nombre de réservations
     */
    long countReservationsByClient(Integer clientId);

    // ===========================================================
    // ==========        ACTIONS MÉTIER                     ======
    // ===========================================================

    /**
     * Confirme une réservation (change le statut à CONFIRMEE).
     *
     * @param id l'identifiant de la réservation à confirmer
     * @return la réservation confirmée
     * @throws com.logondigital.bozacm.exceptions.RessourceNotFoundException si la réservation n'existe pas
     */
    T confirmerReservation(Integer id);

    /**
     * Annule une réservation (change le statut à ANNULEE).
     *
     * @param id l'identifiant de la réservation à annuler
     * @return la réservation annulée
     * @throws com.logondigital.bozacm.exceptions.RessourceNotFoundException si la réservation n'existe pas
     */
    T annulerReservation(Integer id);
}