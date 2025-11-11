package com.logondigital.bozacm.repository.reservation;

import com.logondigital.bozacm.entities.reservation.ReservationTrain;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.ClasseTrain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository pour les réservations de train.
 * Gère les opérations CRUD et les requêtes spécifiques aux trains.
 */
@Repository
public interface ReservationTrainRepo extends JpaRepository<ReservationTrain, Integer> {

    // ==================== MÉTHODES HÉRITÉES DU STYLE ReservationRepo ====================

    /**
     * Trouve toutes les réservations de train d'un client, triées par date de création (plus récent en premier).
     * Utilisé pour afficher l'HISTORIQUE COMPLET des réservations de train.
     *
     * @param clientId L'ID du client
     * @return Liste des réservations de train du client, triée par date décroissante

     * SQL généré :
     * SELECT * FROM reservations r
     * JOIN reservation_train t ON r.id_reservation = t.id_reservation
     * WHERE r.client_id = ? AND r.type_transport = 'TRAIN'
     * ORDER BY r.created_at DESC
     */
    List<ReservationTrain> findByClientIdClientOrderByCreatedAtDesc(Integer clientId);


    /**
     * Trouve les réservations de train d'un client par statut.
     * Exemple : Toutes les réservations de train EN_ATTENTE, ou CONFIRMEE, etc.
     *
     * @param clientId L'ID du client
     * @param statut Le statut recherché
     * @return Liste des réservations correspondantes

     * SQL généré :
     * SELECT * FROM reservations r
     * JOIN reservation_train t ON r.id_reservation = t.id_reservation
     * WHERE r.client_id = ? AND r.statut_reservation = ?
     */
    List<ReservationTrain> findByClientIdClientAndStatutReservation(Integer clientId, StatutReservation statut);


    /**
     * Trouve les réservations de train PASSÉES d'un client (voyages déjà effectués).
     * Utilisé pour l'historique des voyages en train terminés.
     *
     * @param clientId L'ID du client
     * @param now La date/heure actuelle
     * @return Liste des réservations de train dont la date de départ est passée
     */
    @Query("SELECT r FROM ReservationTrain r WHERE r.client.idClient = :clientId AND r.dateDepart < :now ORDER BY r.dateDepart DESC")
    List<ReservationTrain> findReservationsPassees(@Param("clientId") Integer clientId, @Param("now") LocalDateTime now);


    /**
     * Trouve les réservations de train À VENIR d'un client (voyages futurs).
     * Utilisé pour afficher les prochains voyages en train.
     *
     * @param clientId L'ID du client
     * @param now La date/heure actuelle
     * @return Liste des réservations de train dont la date de départ est future
     */
    @Query("SELECT r FROM ReservationTrain r WHERE r.client.idClient = :clientId AND r.dateDepart >= :now ORDER BY r.dateDepart ASC")
    List<ReservationTrain> findReservationsAVenir(@Param("clientId") Integer clientId, @Param("now") LocalDateTime now);


    /**
     * Trouve toutes les réservations de train pour une ville de départ spécifique.
     * Utile pour les statistiques des gares.
     *
     * @param villeDepart La ville de départ
     * @return Liste des réservations de train
     */
    List<ReservationTrain> findByVilleDeDepart(String villeDepart);


    /**
     * Trouve toutes les réservations de train pour un trajet spécifique (départ → arrivée).
     *
     * @param villeDepart La ville de départ
     * @param villeArrivee La ville d'arrivée
     * @return Liste des réservations de train
     */
    List<ReservationTrain> findByVilleDeDepartAndVilleArrivee(String villeDepart, String villeArrivee);


    /**
     * Compte le nombre de réservations de train d'un client.
     *
     * @param clientId L'ID du client
     * @return Le nombre total de réservations de train
     */
    long countByClientIdClient(Integer clientId);


    // ==================== MÉTHODES SPÉCIFIQUES AUX TRAINS ====================

    /**
     * Récupère les réservations de train par compagnie.
     *
     * @param compagnie Nom de la compagnie de train (ex: "CAMRAIL", "SNCF")
     * @return Liste des réservations avec cette compagnie

     * Cas d'usage :
     * - Statistiques par compagnie
     * - Filtrage des réservations dans le dashboard admin
     */
    List<ReservationTrain> findByCompagnieTrain(String compagnie);


    /**
     * Récupère les réservations de train par classe (PREMIERE, SECONDE).
     *
     * @param classe Classe de voyage
     * @return Liste des réservations avec cette classe

     * Cas d'usage :
     * - Voir toutes les réservations en première classe
     * - Statistiques par classe
     */
    List<ReservationTrain> findByClasseTrain(ClasseTrain classe);



    /**
     * Récupère les réservations de train dans un wagon spécifique.
     *
     * @param numeroWagon Numéro du wagon (ex: "A", "B", "1")
     * @return Liste des réservations dans ce wagon

     * Cas d'usage :
     * - Vérifier l'occupation d'un wagon
     * - Gestion des places disponibles
     */
    List<ReservationTrain> findByNumeroWagon(String numeroWagon);


    /**
     * Trouve les réservations de train pour un trajet ET une classe spécifiques.
     * Exemple : Toutes les réservations première classe Yaoundé → Ngaoundéré
     *
     * @param villeDepart Ville de départ
     * @param villeArrivee Ville d'arrivée
     * @param classe Classe de train
     * @return Liste des réservations
     */
    List<ReservationTrain> findByVilleDeDepartAndVilleArriveeAndClasseTrain(
            String villeDepart,
            String villeArrivee,
            ClasseTrain classe
    );


    /**
     * Récupère les réservations de train d'un client pour une compagnie spécifique.
     * Utile pour l'historique client filtré par compagnie.
     *
     * @param clientId ID du client
     * @param compagnie Nom de la compagnie
     * @return Liste des réservations
     */
    List<ReservationTrain> findByClientIdClientAndCompagnieTrain(Integer clientId, String compagnie);


    /**
     * Récupère les réservations de train d'un client dans une classe spécifique.
     *
     * @param clientId ID du client
     * @param classe Classe de train
     * @return Liste des réservations
     */
    List<ReservationTrain> findByClientIdClientAndClasseTrain(Integer clientId, ClasseTrain classe);


    /**
     * Compte le nombre de réservations pour une compagnie de train.
     * Utile pour les statistiques de popularité des compagnies.
     *
     * @param compagnie Nom de la compagnie
     * @return Nombre de réservations
     */
    long countByCompagnieTrain(String compagnie);


    /**
     * Compte le nombre de réservations par classe de train.
     *
     * @param classe Classe de train
     * @return Nombre de réservations
     */
    long countByClasseTrain(ClasseTrain classe);


    /**
     * Compte le nombre de réservations dans un wagon spécifique.
     * Utile pour vérifier l'occupation des wagons.
     *
     * @param numeroWagon Numéro du wagon
     * @return Nombre de réservations
     */
    long countByNumeroWagon(String numeroWagon);


    /**
     * Récupère toutes les compagnies de train distinctes.
     * Utile pour afficher une liste de compagnies disponibles.
     *
     * @return liste des noms de compagnies uniques
     */
    @Query("SELECT DISTINCT r.compagnieTrain FROM ReservationTrain r")
    List<String> findAllCompagniesDistinctes();


    /**
     * Recherche les réservations par compagnie ET classe.
     *
     * @param compagnie le nom de la compagnie
     * @param classeTrain la classe de train
     * @return liste des réservations correspondant aux deux critères
     */
    List<ReservationTrain> findByCompagnieTrainAndClasseTrain(String compagnie, ClasseTrain classeTrain);


    /**
     * Vérifie si une compagnie existe dans les réservations.
     *
     * @param compagnie le nom de la compagnie
     * @return true si au moins une réservation existe pour cette compagnie
     */
    boolean existsByCompagnieTrain(String compagnie);


    /**
     * Récupère toutes les réservations pour un wagon spécifique d'une compagnie.
     * Permet de voir l'occupation d'un wagon.
     *
     * @param compagnie le nom de la compagnie
     * @param numeroWagon le numéro du wagon
     * @return liste des réservations pour ce wagon
     */
    List<ReservationTrain> findByCompagnieTrainAndNumeroWagon(String compagnie, String numeroWagon);
}