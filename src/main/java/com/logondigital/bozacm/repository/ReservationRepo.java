package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Reservation;
import com.logondigital.bozacm.enums.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Un Repository est une interface qui permet de communiquer avec la base de données sans écrire de SQL.

 *   Repository pour gérer les opérations sur l'entité Reservation.
 *   Fournit des méthodes pour récupérer l'historique des réservations,
 *   filtrer par statut, et gérer les réservations passées/à venir.
 */
public interface ReservationRepo extends JpaRepository<Reservation, Integer> {

    /**
     * Trouve toutes les réservations d'un client, triées par date de création (plus récent en premier).
     * Utilisé pour afficher l'HISTORIQUE COMPLET des réservations.

     * @param clientId L'ID du client
     * @return Liste des réservations du client, triée par date décroissante

     * SQL généré :
     * SELECT * FROM reservations WHERE client_id = ? ORDER BY created_at DESC

     * Nom de la méthode en français:
     * Rechercher par l'attribut "Client" dans Reservation l'attribut "idClient" dans Client trier par l'attribut "CreatedAt" par ordre croissant(du plus récent au plus ancient)
     */
    List<Reservation> findByClientIdClientOrderByCreatedAtDesc(Integer clientId);



    /**
     * Trouve les réservations d'un client par statut.
     * Exemple : Toutes les réservations EN_ATTENTE, ou CONFIRMEE, etc.

     * @param clientId L'ID du client
     * @param statut Le statut recherché
     * @return Liste des réservations correspondantes

     * SQL généré :
     * SELECT * FROM reservations WHERE client_id = ? AND statut_reservation = ?
     */
    List<Reservation> findByClientIdClientAndStatutReservation(Integer clientId, StatutReservation statut);


    /**
     * Trouve les réservations PASSÉES d'un client (voyages déjà effectués).
     * Utilisé pour l'historique des voyages terminés.
     *
     * @param clientId L'ID du client
     * @param now La date/heure actuelle
     * @return Liste des réservations dont la date de départ est passée

     * Quand utiliser @Query ?
     * Note : Utilise @Query car la logique est plus complexe (comparaison de dates)
     */
    @Query("SELECT r FROM Reservation r WHERE r.client.idClient = :clientId AND r.dateDepart < :now ORDER BY r.dateDepart DESC")
    List<Reservation> findReservationsPassees(@Param("clientId") Integer clientId, @Param("now") LocalDateTime now);



    /**
     * Trouve les réservations À VENIR d'un client (voyages futurs).
     * Utilisé pour afficher les prochains voyages.
     *
     * @param clientId L'ID du client
     * @param now La date/heure actuelle
     * @return Liste des réservations dont la date de départ est future

     * Quand utiliser @Query ?
     *  Quand la logique est trop complexe pour le naming convention.
     */
    @Query("SELECT r FROM Reservation r WHERE r.client.idClient = :clientId AND r.dateDepart >= :now ORDER BY r.dateDepart ASC")
    List<Reservation> findReservationsAVenir(@Param("clientId") Integer clientId, @Param("now") LocalDateTime now);



    /**
     * Trouve toutes les réservations pour une ville de départ spécifique.
     * Utile pour les statistiques des agences.
     *
     * @param villeDepart La ville de départ
     * @return Liste des réservations
     */
    List<Reservation> findByVilleDeDepart(String villeDepart);



    /**
     * Trouve toutes les réservations pour un trajet spécifique (départ → arrivée).
     *
     * @param villeDepart La ville de départ
     * @param villeArrivee La ville d'arrivée
     * @return Liste des réservations
     */
    List<Reservation> findByVilleDeDepartAndVilleArrivee(String villeDepart, String villeArrivee);



    /**
     * Compte le nombre de réservations d'un client.
     *
     * @param clientId L'ID du client
     * @return Le nombre total de réservations
     */
    long countByClientIdClient(Integer clientId);

}
