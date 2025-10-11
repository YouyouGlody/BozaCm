package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Billet;
import com.logondigital.bozacm.enums.StatutBillet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Un Repository est une interface qui permet de communiquer avec la base de données sans écrire de SQL.
 * Repository pour gérer les opérations sur l'entité Billet.
 * Fournit des méthodes pour rechercher les billets par client, statut, numéro, etc.
 */
public interface BilletRepo extends JpaRepository<Billet, Integer> {

    /**
     * Trouve un billet par son numéro unique.
     * Utilisé pour la validation du billet lors de l'embarquement (scan QR code).

     * @param numeroBillet Le numéro unique du billet (format: BZC-xxx)
     * @return Optional<Billet> contenant le billet s'il existe

     * SQL généré :
     * SELECT * FROM billets WHERE numero_billet = ?
     */
    Optional<Billet> findByNumeroBillet(String numeroBillet);


    /**
     * Trouve tous les billets d'un client, triés par date d'émission (plus récent en premier).

     * @param clientId L'ID du client
     * @return Liste des billets du client

     * SQL généré :
     * SELECT * FROM billets WHERE client_id = ? ORDER BY date_emission DESC
     */
    List<Billet> findByClientIdClientOrderByDateEmissionDesc(Integer clientId);


    /**
     * Trouve les billets d'un client par statut.
     * Exemple : Tous les billets VALIDE, ou EXPIRE, etc.
     *
     * @param clientId L'ID du client
     * @param statut Le statut recherché
     * @return Liste des billets correspondants
     */
    List<Billet> findByClientIdClientAndStatutBillet(Integer clientId, StatutBillet statut);

    /**
     * Trouve un billet par l'ID de sa réservation.
     * Relation One-to-One : Une réservation = Un billet.
     *
     * @param reservationId L'ID de la réservation
     * @return Optional<Billet> contenant le billet s'il existe
     */
    Optional<Billet> findByReservationIdReservation(Integer reservationId);

    /**
     * Trouve tous les billets expirés avant une certaine date.
     * Utile pour un job de nettoyage ou des statistiques.
     *
     * @param date La date limite
     * @return Liste des billets expirés
     */
    List<Billet> findByDateExpirationBefore(LocalDateTime date);

    /**
     * Compte le nombre de billets d'un client.
     *
     * @param clientId L'ID du client
     * @return Le nombre total de billets
     */
    long countByClientIdClient(Integer clientId);

    /**
     * Vérifie si un billet existe avec ce numéro.
     *
     * @param numeroBillet Le numéro du billet
     * @return true si le billet existe, false sinon
     */
    boolean existsByNumeroBillet(String numeroBillet);


}
