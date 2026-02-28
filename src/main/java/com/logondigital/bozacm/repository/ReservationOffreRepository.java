package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.ReservationOffre;
import com.logondigital.bozacm.entities.ReservationOffre.StatutReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationOffreRepository extends JpaRepository<ReservationOffre, Integer> {

    // ─── Recherches par offre ──────────────────────────────────────────────────

    /**
     * Récupère toutes les réservations d'une offre donnée, paginées.
     */
    @Query("SELECT r FROM ReservationOffre r WHERE r.offre.id = :offreId")
    Page<ReservationOffre> findByOffreId(@Param("offreId") Integer offreId, Pageable pageable);

    /**
     * Compte les réservations confirmées pour une offre.
     */
    @Query("SELECT COUNT(r) FROM ReservationOffre r WHERE r.offre.id = :offreId AND r.statut = :statut")
    Long countByOffreIdAndStatut(
            @Param("offreId") Integer offreId,
            @Param("statut")  StatutReservation statut
    );

    // ─── Recherches par client ─────────────────────────────────────────────────

    /**
     * Récupère toutes les réservations d'un client par son email (insensible à la casse).
     */
    @Query("SELECT r FROM ReservationOffre r WHERE LOWER(r.emailClient) = LOWER(:email)")
    List<ReservationOffre> findByEmailClientIgnoreCase(@Param("email") String email);

    /**
     * Recherche les réservations d'un client par son nom (insensible à la casse, partiel).
     */
    @Query("SELECT r FROM ReservationOffre r WHERE LOWER(r.nomClient) LIKE LOWER(CONCAT('%', :nom, '%'))")
    Page<ReservationOffre> findByNomClientIgnoreCase(@Param("nom") String nom, Pageable pageable);

    /**
     * Vérifie si un client a déjà réservé une offre spécifique.
     * Utile pour éviter les réservations doublons.
     */
    @Query("""
            SELECT COUNT(r) > 0 FROM ReservationOffre r
            WHERE LOWER(r.emailClient) = LOWER(:email)
            AND   r.offre.id = :offreId
            """)
    boolean existsByEmailClientAndOffreId(
            @Param("email")   String email,
            @Param("offreId") Integer offreId
    );

    // ─── Recherches par statut ─────────────────────────────────────────────────

    /**
     * Récupère toutes les réservations par statut, paginées.
     */
    @Query("SELECT r FROM ReservationOffre r WHERE r.statut = :statut")
    Page<ReservationOffre> findByStatut(@Param("statut") StatutReservation statut, Pageable pageable);

    /**
     * Mise à jour du statut d'une réservation en masse pour une offre.
     * Exemple : annuler toutes les réservations EN_ATTENTE d'une offre supprimée.
     */
    @Modifying
    @Transactional
    @Query("""
            UPDATE ReservationOffre r
            SET r.statut = :nouveauStatut
            WHERE r.offre.id = :offreId
            AND   r.statut   = :ancienStatut
            """)
    int updateStatutByOffreId(
            @Param("offreId")       Integer offreId,
            @Param("ancienStatut")  StatutReservation ancienStatut,
            @Param("nouveauStatut") StatutReservation nouveauStatut
    );

    // ─── Recherches par date ───────────────────────────────────────────────────

    /**
     * Récupère les réservations faites entre deux dates.
     */
    @Query("""
            SELECT r FROM ReservationOffre r
            WHERE r.dateReservation BETWEEN :debut AND :fin
            ORDER BY r.dateReservation DESC
            """)
    List<ReservationOffre> findByDateReservationBetween(
            @Param("debut") LocalDate debut,
            @Param("fin")   LocalDate fin
    );

    // ─── Recherches par agence ─────────────────────────────────────────────────

    /**
     * Récupère toutes les réservations des offres d'une agence.
     */
    @Query("SELECT r FROM ReservationOffre r WHERE r.offre.agence.id = :agenceId")
    Page<ReservationOffre> findByAgenceId(@Param("agenceId") Integer agenceId, Pageable pageable);

    /**
     * Récupère les réservations confirmées d'une agence (pour le chiffre d'affaires).
     */
    @Query("""
            SELECT r FROM ReservationOffre r
            WHERE r.offre.agence.id = :agenceId
            AND   r.statut = 'CONFIRMEE'
            """)
    List<ReservationOffre> findReservationsConirmeesByAgenceId(@Param("agenceId") Integer agenceId);

    // ─── Statistiques ──────────────────────────────────────────────────────────

    /**
     * Chiffre d'affaires total d'une agence (somme des prix des offres confirmées).
     */
    @Query("""
            SELECT COALESCE(SUM(o.prix), 0)
            FROM ReservationOffre r
            JOIN r.offre o
            WHERE o.agence.id = :agenceId
            AND   r.statut = 'CONFIRMEE'
            """)
    Double findChiffreAffaireByAgenceId(@Param("agenceId") Integer agenceId);

    /**
     * Nombre de réservations par statut pour une agence donnée.
     */
    @Query("""
            SELECT r.statut, COUNT(r)
            FROM ReservationOffre r
            WHERE r.offre.agence.id = :agenceId
            GROUP BY r.statut
            """)
    List<Object[]> countByStatutForAgence(@Param("agenceId") Integer agenceId);


    // ─── Rapport global ────────────────────────────────────────────────────────

    /** Nombre total de réservations */
    @Query("SELECT COUNT(r) FROM ReservationOffre r")
    Long countTotal();

    /** Nombre de réservations par statut */
    @Query("SELECT COUNT(r) FROM ReservationOffre r WHERE r.statut = :statut")
    Long countByStatut(@Param("statut") StatutReservation statut);

    /** Chiffre d'affaires total — somme des prix des offres CONFIRMEES */
    @Query("SELECT COALESCE(SUM(r.offre.prix), 0) FROM ReservationOffre r WHERE r.statut = 'CONFIRMEE'")
    Double getTotalChiffreAffaires();

    /** Offre la plus réservée — nom de l'offre avec le plus de réservations */
    @Query("""
            SELECT r.offre.titre FROM ReservationOffre r
            GROUP BY r.offre.titre
            ORDER BY COUNT(r) DESC
            LIMIT 1
            """)
    String findOffreLaPlusReservee();

    /** Agence la plus active — celle avec le plus de réservations confirmées */
    @Query("""
            SELECT r.offre.agence.nom FROM ReservationOffre r
            WHERE r.statut = 'CONFIRMEE'
            GROUP BY r.offre.agence.nom
            ORDER BY COUNT(r) DESC
            LIMIT 1
            """)
    String findAgenceLaPlusActive();

    /** Trajet le plus emprunté — depart → arrivee avec le plus de réservations */
    @Query("""
            SELECT CONCAT(r.offre.trajet.depart, ' → ', r.offre.trajet.arrivee)
            FROM ReservationOffre r
            GROUP BY r.offre.trajet.depart, r.offre.trajet.arrivee
            ORDER BY COUNT(r) DESC
            LIMIT 1
            """)
    String findTrajetLePlusEmprunte();

}