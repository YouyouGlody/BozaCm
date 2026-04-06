package com.logondigital.bozacm.repository.reservation;

import com.logondigital.bozacm.entities.reservation.ReservationBus;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.TypeBus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository pour les réservations de bus.
 * Gère les opérations CRUD et les requêtes spécifiques aux bus.
 */
@Repository
public interface ReservationBusRepo extends JpaRepository<ReservationBus, Integer> {

    // ==================== MÉTHODES HÉRITÉES DU STYLE ReservationRepo ====================

    /**
     * Trouve toutes les réservations de bus d'un client, triées par date de création (plus récent en premier).
     * Utilisé pour afficher l'HISTORIQUE COMPLET des réservations de bus.
     *
     * @param clientId L'ID du client
     * @return Liste des réservations de bus du client, triée par date décroissante

     * SQL généré :
     * SELECT * FROM reservations r
     * JOIN reservation_bus b ON r.id_reservation = b.id_reservation
     * WHERE r.client_id = ? AND r.type_transport = 'BUS'
     * ORDER BY r.created_at DESC
     */
    List<ReservationBus> findByClientIdClientOrderByCreatedAtDesc(Integer clientId);


    /**
     * Trouve les réservations de bus d'un client par statut.
     * Exemple : Toutes les réservations de bus EN_ATTENTE, ou CONFIRMEE, etc.
     *
     * @param clientId L'ID du client
     * @param statut Le statut recherché
     * @return Liste des réservations correspondantes

     * SQL généré :
     * SELECT * FROM reservations r
     * JOIN reservation_bus b ON r.id_reservation = b.id_reservation
     * WHERE r.client_id = ? AND r.statut_reservation = ?
     */
    List<ReservationBus> findByClientIdClientAndStatutReservation(Integer clientId, StatutReservation statut);


    /**
     * Trouve les réservations de bus PASSÉES d'un client (voyages déjà effectués).
     * Utilisé pour l'historique des voyages en bus terminés.
     *
     * @param clientId L'ID du client
     * @param now La date/heure actuelle
     * @return Liste des réservations de bus dont la date de départ est passée
     */
    @Query("SELECT r FROM ReservationBus r WHERE r.client.idClient = :clientId AND r.dateDepart < :now ORDER BY r.dateDepart DESC")
    List<ReservationBus> findReservationsPassees(@Param("clientId") Integer clientId, @Param("now") LocalDateTime now);


    /**
     * Trouve les réservations de bus À VENIR d'un client (voyages futurs).
     * Utilisé pour afficher les prochains voyages en bus.
     *
     * @param clientId L'ID du client
     * @param now La date/heure actuelle
     * @return Liste des réservations de bus dont la date de départ est future
     */
    @Query("SELECT r FROM ReservationBus r WHERE r.client.idClient = :clientId AND r.dateDepart >= :now ORDER BY r.dateDepart ASC")
    List<ReservationBus> findReservationsAVenir(@Param("clientId") Integer clientId, @Param("now") LocalDateTime now);


    /**
     * Trouve toutes les réservations de bus pour une ville de départ spécifique.
     * Utile pour les statistiques des agences.
     *
     * @param villeDepart La ville de départ
     * @return Liste des réservations de bus
     */
    List<ReservationBus> findByVilleDeDepart(String villeDepart);


    /**
     * Trouve toutes les réservations de bus pour un trajet spécifique (départ → arrivée).
     *
     * @param villeDepart La ville de départ
     * @param villeArrivee La ville d'arrivée
     * @return Liste des réservations de bus
     */
    List<ReservationBus> findByVilleDeDepartAndVilleArrivee(String villeDepart, String villeArrivee);


    /**
     * Compte le nombre de réservations de bus d'un client.
     *
     * @param clientId L'ID du client
     * @return Le nombre total de réservations de bus
     */
    long countByClientIdClient(Integer clientId);


    // ==================== MÉTHODES SPÉCIFIQUES AUX BUS ====================

    /**
     * Récupère les réservations de bus par compagnie.
     *
     * @param compagnie Nom de la compagnie de bus (ex: "Touristique Express")
     * @return Liste des réservations avec cette compagnie

     * Cas d'usage :
     * - Statistiques par compagnie
     * - Filtrage des réservations dans le dashboard admin
     */
    List<ReservationBus> findByCompagnieBus(String compagnie);


    /**
     * Récupère les réservations de bus par type (STANDARD, VIP, COUCHETTE).
     *
     * @param typeBus Type de bus
     * @return Liste des réservations avec ce type de bus

     * Cas d'usage :
     * - Voir toutes les réservations VIP
     * - Statistiques par type de bus
     */
    List<ReservationBus> findByTypeBus(TypeBus typeBus);


    /**
     * Récupère les réservations de bus avec/sans climatisation.
     *
     * @param climatisation true pour avec clim, false pour sans
     * @return Liste des réservations

     * Cas d'usage :
     * - Filtrer les bus climatisés
     * - Statistiques sur les préférences clients
     */
    List<ReservationBus> findByClimatisation(Boolean climatisation);


    /**
     * Récupère les réservations de bus d'un type spécifique avec climatisation.
     * Exemple : Tous les bus VIP climatisés
     *
     * @param typeBus Type de bus
     * @param climatisation true/false
     * @return Liste des réservations
     */
    List<ReservationBus> findByTypeBusAndClimatisation(TypeBus typeBus, Boolean climatisation);


    /**
     * Trouve les réservations de bus pour un trajet ET un type de bus spécifiques.
     * Exemple : Tous les bus VIP Douala → Yaoundé
     *
     * @param villeDepart Ville de départ
     * @param villeArrivee Ville d'arrivée
     * @param typeBus Type de bus
     * @return Liste des réservations
     */
    List<ReservationBus> findByVilleDeDepartAndVilleArriveeAndTypeBus(
            String villeDepart,
            String villeArrivee,
            TypeBus typeBus
    );


    /**
     * Récupère les réservations de bus d'un client pour une compagnie spécifique.
     * Utile pour l'historique client filtré par compagnie.
     *
     * @param clientId ID du client
     * @param compagnie Nom de la compagnie
     * @return Liste des réservations
     */
    List<ReservationBus> findByClientIdClientAndCompagnieBus(Integer clientId, String compagnie);


    /**
     * Compte le nombre de réservations pour une compagnie de bus.
     * Utile pour les statistiques de popularité des compagnies.
     *
     * @param compagnie Nom de la compagnie
     * @return Nombre de réservations
     */
    long countByCompagnieBus(String compagnie);


    /**
     * Compte le nombre de réservations par type de bus.
     *
     * @param typeBus Type de bus
     * @return Nombre de réservations
     */
    long countByTypeBus(TypeBus typeBus);



    /**
     * Recherche les réservations par compagnie ET type de bus.
     *
     * @param compagnie le nom de la compagnie
     * @param typeBus le type de bus
     * @return liste des réservations correspondant aux deux critères
     */
    List<ReservationBus> findByCompagnieBusAndTypeBus(String compagnie, TypeBus typeBus);



    /**
     * Recherche les réservations VIP avec climatisation pour un client.
     * Exemple de méthode métier combinant plusieurs critères.
     *
     * @param clientId l'identifiant du client
     * @return liste des réservations VIP climatisées du client
     */
    List<ReservationBus> findByClientIdClientAndTypeBusAndClimatisation(Integer clientId, TypeBus typeBus, boolean b);


    /**
     * Vérifie si une compagnie existe dans les réservations.
     *
     * @param compagnie le nom de la compagnie
     * @return true si au moins une réservation existe pour cette compagnie
     */
    boolean existsByCompagnieBus(String compagnie);
}