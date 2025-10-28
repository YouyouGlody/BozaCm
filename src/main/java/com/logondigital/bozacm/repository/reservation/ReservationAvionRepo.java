package com.logondigital.bozacm.repository.reservation;

import com.logondigital.bozacm.entities.reservation.ReservationAvion;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.ClasseAvion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository pour les réservations d'avion.
 * Gère les opérations CRUD et les requêtes spécifiques aux vols.
 */
@Repository
public interface ReservationAvionRepo extends JpaRepository<ReservationAvion, Integer> {

    // ==================== MÉTHODES HÉRITÉES DU STYLE ReservationRepo ====================

    /**
     * Trouve toutes les réservations d'avion d'un client, triées par date de création (plus récent en premier).
     * Utilisé pour afficher l'HISTORIQUE COMPLET des réservations d'avion.
     *
     * @param clientId L'ID du client
     * @return Liste des réservations d'avion du client, triée par date décroissante

     * SQL généré :
     * SELECT * FROM reservations r
     * JOIN reservation_avion a ON r.id_reservation = a.id_reservation
     * WHERE r.client_id = ? AND r.type_transport = 'AVION'
     * ORDER BY r.created_at DESC
     */
    List<ReservationAvion> findByClientIdClientOrderByCreatedAtDesc(Integer clientId);


    /**
     * Trouve les réservations d'avion d'un client par statut.
     * Exemple : Toutes les réservations d'avion EN_ATTENTE, ou CONFIRMEE, etc.
     *
     * @param clientId L'ID du client
     * @param statut Le statut recherché
     * @return Liste des réservations correspondantes

     * SQL généré :
     * SELECT * FROM reservations r
     * JOIN reservation_avion a ON r.id_reservation = a.id_reservation
     * WHERE r.client_id = ? AND r.statut_reservation = ?
     */
    List<ReservationAvion> findByClientIdClientAndStatutReservation(Integer clientId, StatutReservation statut);


    /**
     * Trouve les réservations d'avion PASSÉES d'un client (vols déjà effectués).
     * Utilisé pour l'historique des vols terminés.
     *
     * @param clientId L'ID du client
     * @param now La date/heure actuelle
     * @return Liste des réservations d'avion dont la date de départ est passée
     */
    @Query("SELECT r FROM ReservationAvion r WHERE r.client.idClient = :clientId AND r.dateDepart < :now ORDER BY r.dateDepart DESC")
    List<ReservationAvion> findReservationsPassees(@Param("clientId") Integer clientId, @Param("now") LocalDateTime now);


    /**
     * Trouve les réservations d'avion À VENIR d'un client (vols futurs).
     * Utilisé pour afficher les prochains vols.
     *
     * @param clientId L'ID du client
     * @param now La date/heure actuelle
     * @return Liste des réservations d'avion dont la date de départ est future
     */
    @Query("SELECT r FROM ReservationAvion r WHERE r.client.idClient = :clientId AND r.dateDepart >= :now ORDER BY r.dateDepart ASC")
    List<ReservationAvion> findReservationsAVenir(@Param("clientId") Integer clientId, @Param("now") LocalDateTime now);


    /**
     * Trouve toutes les réservations d'avion pour une ville de départ spécifique.
     * Utile pour les statistiques des aéroports.
     *
     * @param villeDepart La ville de départ
     * @return Liste des réservations d'avion
     */
    List<ReservationAvion> findByVilleDeDepart(String villeDepart);


    /**
     * Trouve toutes les réservations d'avion pour un trajet spécifique (départ → arrivée).
     *
     * @param villeDepart La ville de départ
     * @param villeArrivee La ville d'arrivée
     * @return Liste des réservations d'avion
     */
    List<ReservationAvion> findByVilleDeDepartAndVilleArrivee(String villeDepart, String villeArrivee);


    /**
     * Compte le nombre de réservations d'avion d'un client.
     *
     * @param clientId L'ID du client
     * @return Le nombre total de réservations d'avion
     */
    long countByClientIdClient(Integer clientId);


    // ==================== MÉTHODES SPÉCIFIQUES AUX AVIONS ====================

    /**
     * Récupère les réservations d'avion par compagnie aérienne.
     *
     * @param compagnie Nom de la compagnie aérienne (ex: "Air France", "Camair-Co")
     * @return Liste des réservations avec cette compagnie

     * Cas d'usage :
     * - Statistiques par compagnie
     * - Filtrage des réservations dans le dashboard admin
     */
    List<ReservationAvion> findByCompagnieAerienne(String compagnie);


    /**
     * Récupère les réservations d'avion par numéro de vol.
     *
     * @param numeroVol Numéro du vol (ex: "AF1234", "KL890")
     * @return Liste des réservations pour ce vol

     * Cas d'usage :
     * - Voir tous les passagers d'un vol
     * - Vérifier l'occupation d'un vol
     */
    List<ReservationAvion> findByNumeroVol(String numeroVol);


    /**
     * Récupère les réservations d'avion par classe (ECONOMIE, AFFAIRES, PREMIERE).
     *
     * @param classe Classe de voyage
     * @return Liste des réservations avec cette classe

     * Cas d'usage :
     * - Voir toutes les réservations en classe affaires
     * - Statistiques par classe
     */
    List<ReservationAvion> findByClasseAvion(ClasseAvion classe);


    /**
     * Récupère les réservations d'avion d'un terminal spécifique.
     *
     * @param terminal Numéro du terminal (ex: "2E", "1")
     * @return Liste des réservations pour ce terminal

     * Cas d'usage :
     * - Gestion des flux passagers par terminal
     * - Statistiques par terminal
     */
    List<ReservationAvion> findByNumeroTerminal(String terminal);


    /**
     * Récupère les réservations d'avion avec un poids de bagages minimum.
     *
     * @param poidsMin Poids minimum autorisé (en kg)
     * @return Liste des réservations

     * Cas d'usage :
     * - Filtrer les vols avec bagages importants autorisés
     */
    @Query("SELECT r FROM ReservationAvion r WHERE r.poidsMaxBagages >= :poidsMin")
    List<ReservationAvion> findByPoidsMaxBagagesGreaterThanEqual(@Param("poidsMin") Integer poidsMin);


    /**
     * Récupère les réservations d'avion d'une classe spécifique avec un poids de bagages minimum.
     * Exemple : Toutes les réservations classe affaires avec 40kg de bagages
     *
     * @param classe Classe d'avion
     * @param poidsMin Poids minimum autorisé
     * @return Liste des réservations
     */
    @Query("SELECT r FROM ReservationAvion r WHERE r.classeAvion = :classe AND r.poidsMaxBagages >= :poidsMin")
    List<ReservationAvion> findByClasseAvionAndPoidsMaxBagages(
            @Param("classe") ClasseAvion classe,
            @Param("poidsMin") Integer poidsMin
    );


    /**
     * Trouve les réservations d'avion pour un trajet ET une classe spécifiques.
     * Exemple : Toutes les réservations classe économie Douala → Paris
     *
     * @param villeDepart Ville de départ
     * @param villeArrivee Ville d'arrivée
     * @param classe Classe d'avion
     * @return Liste des réservations
     */
    List<ReservationAvion> findByVilleDeDepartAndVilleArriveeAndClasseAvion(
            String villeDepart,
            String villeArrivee,
            ClasseAvion classe
    );


    /**
     * Récupère les réservations d'avion pour un vol et une date spécifiques.
     * Utile pour vérifier la disponibilité des sièges sur un vol précis.
     *
     * @param numeroVol Numéro du vol
     * @param dateDepart Date de départ (seule la date compte, pas l'heure)
     * @return Liste des réservations pour ce vol à cette date
     */
    @Query("SELECT r FROM ReservationAvion r WHERE r.numeroVol = :numeroVol AND DATE(r.dateDepart) = DATE(:dateDepart)")
    List<ReservationAvion> findByNumeroVolAndDate(
            @Param("numeroVol") String numeroVol,
            @Param("dateDepart") LocalDateTime dateDepart
    );


    /**
     * Récupère les réservations d'avion d'un client pour une compagnie spécifique.
     * Utile pour l'historique client filtré par compagnie.
     *
     * @param clientId ID du client
     * @param compagnie Nom de la compagnie
     * @return Liste des réservations
     */
    List<ReservationAvion> findByClientIdClientAndCompagnieAerienne(Integer clientId, String compagnie);


    /**
     * Récupère les réservations d'avion d'un client dans une classe spécifique.
     *
     * @param clientId ID du client
     * @param classe Classe d'avion
     * @return Liste des réservations
     */
    List<ReservationAvion> findByClientIdClientAndClasseAvion(Integer clientId, ClasseAvion classe);


    /**
     * Compte le nombre de réservations pour une compagnie aérienne.
     * Utile pour les statistiques de popularité des compagnies.
     *
     * @param compagnie Nom de la compagnie
     * @return Nombre de réservations
     */
    long countByCompagnieAerienne(String compagnie);


    /**
     * Compte le nombre de réservations par classe d'avion.
     *
     * @param classe Classe d'avion
     * @return Nombre de réservations
     */
    long countByClasseAvion(ClasseAvion classe);


    /**
     * Compte le nombre de réservations pour un vol spécifique.
     * Utile pour vérifier l'occupation d'un vol.
     *
     * @param numeroVol Numéro du vol
     * @return Nombre de réservations
     */
    long countByNumeroVol(String numeroVol);


    /**
     * Compte le nombre de réservations dans un terminal spécifique.
     *
     * @param terminal Numéro du terminal
     * @return Nombre de réservations
     */
    long countByNumeroTerminal(String terminal);
}