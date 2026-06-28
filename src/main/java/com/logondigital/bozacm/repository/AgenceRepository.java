package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.dto.StatistiquesAgenceDetailDTO;
import com.logondigital.bozacm.entities.Agence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgenceRepository extends JpaRepository<Agence, Integer> {

    // ─── Recherches simples ────────────────────────────────────────────────────

    /**
     * Recherche une agence par son nom exact (insensible à la casse).
     */
    @Query("SELECT a FROM Agence a WHERE LOWER(a.nom) = LOWER(:nom)")
    Optional<Agence> findByNomIgnoreCase(@Param("nom") String nom);

    /**
     * Vérifie si une agence existe avec cet email (insensible à la casse).
     * Utile pour éviter les doublons à la création.
     */
    @Query("SELECT COUNT(a) > 0 FROM Agence a WHERE LOWER(a.email) = LOWER(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);


    @Query("SELECT a FROM Agence a WHERE LOWER(a.email) = LOWER(:email)")
    Optional<Agence> findByEmailIgnoreCase(@Param("email") String email);
    /**
     * Recherche les agences dont le nom contient le mot-clé (insensible à la casse).
     */
    @Query("SELECT a FROM Agence a WHERE LOWER(a.nom) LIKE LOWER(CONCAT('%', :motCle, '%'))")
    Page<Agence> findByNomContainingIgnoreCase(@Param("motCle") String motCle, Pageable pageable);

    /**
     * Recherche les agences par ville/adresse (insensible à la casse).
     */
    @Query("SELECT a FROM Agence a WHERE LOWER(a.adresse) LIKE LOWER(CONCAT('%', :ville, '%'))")
    List<Agence> findByVilleIgnoreCase(@Param("ville") String ville);

    /**
     * Recherche une agence par son numéro de téléphone exact.
     */
    @Query("SELECT a FROM Agence a WHERE a.telephone = :telephone")
    Optional<Agence> findByTelephone(@Param("telephone") String telephone);

    /**
     * Recherche multi-champs : nom, adresse ou email (insensible à la casse).
     */
    @Query("SELECT a FROM Agence a WHERE LOWER(a.nom) LIKE LOWER(CONCAT('%', :terme, '%')) OR LOWER(a.adresse) LIKE LOWER(CONCAT('%', :terme, '%')) OR LOWER(a.email) LIKE LOWER(CONCAT('%', :terme, '%'))")
    List<Agence> rechercher(@Param("terme") String terme);

    // ─── Statistiques ──────────────────────────────────────────────────────────

    /**
     * Récupère les agences ayant au moins une offre active.
     */
    @Query("SELECT DISTINCT a FROM Agence a JOIN a.offres o")
    List<Agence> findAgencesAvecOffres();

    /**
     * Récupère les statistiques détaillées de toutes les agences,
     * triées par chiffre d'affaires décroissant.
     */
    @Query("""
            SELECT new com.logondigital.bozacm.dto.StatistiquesAgenceDetailDTO(
                a.id,
                a.nom,
                a.email,
                a.telephone,
                a.adresse,
                COUNT(DISTINCT o.id),
                AVG(o.prix),
                MIN(o.prix),
                MAX(o.prix),
                COUNT(r.id),
                SUM(CASE WHEN r.statut = 'CONFIRMEE' THEN 1 ELSE 0 END),
                CASE WHEN COUNT(r.id) > 0
                     THEN (SUM(CASE WHEN r.statut = 'CONFIRMEE' THEN 1.0 ELSE 0.0 END) / COUNT(r.id)) * 100
                     ELSE 0.0 END,
                SUM(CASE WHEN r.statut = 'CONFIRMEE' THEN o.prix ELSE 0.0 END),
                0
            )
            FROM Agence a
            LEFT JOIN a.offres o
            LEFT JOIN o.reservationOffres r
            GROUP BY a.id, a.nom, a.email, a.telephone, a.adresse
            ORDER BY SUM(CASE WHEN r.statut = 'CONFIRMEE' THEN o.prix ELSE 0.0 END) DESC
            """)
    List<StatistiquesAgenceDetailDTO> findStatistiquesDetailleesToutesAgences();

    /**
     * Récupère les statistiques détaillées d'une seule agence.
     */
    @Query("""
            SELECT new com.logondigital.bozacm.dto.StatistiquesAgenceDetailDTO(
                a.id,
                a.nom,
                a.email,
                a.telephone,
                a.adresse,
                COUNT(DISTINCT o.id),
                AVG(o.prix),
                MIN(o.prix),
                MAX(o.prix),
                COUNT(r.id),
                SUM(CASE WHEN r.statut = 'CONFIRMEE' THEN 1 ELSE 0 END),
                CASE WHEN COUNT(r.id) > 0
                     THEN (SUM(CASE WHEN r.statut = 'CONFIRMEE' THEN 1.0 ELSE 0.0 END) / COUNT(r.id)) * 100
                     ELSE 0.0 END,
                SUM(CASE WHEN r.statut = 'CONFIRMEE' THEN o.prix ELSE 0.0 END),
                0
            )
            FROM Agence a
            LEFT JOIN a.offres o
            LEFT JOIN o.reservationOffres r
            WHERE a.id = :agenceId
            GROUP BY a.id, a.nom, a.email, a.telephone, a.adresse
            """)
    Optional<StatistiquesAgenceDetailDTO> findStatistiquesParAgenceId(@Param("agenceId") Integer agenceId);

    /**
     * Compte le nombre d'offres actives par agence.
     */
    @Query("SELECT COUNT(o) FROM Offre o WHERE o.agence.id = :agenceId")
    Long countOffresByAgenceId(@Param("agenceId") Integer agenceId);

    /**
     * Top N agences par nombre de réservations confirmées.
     */
    @Query("""
            SELECT a FROM Agence a
            JOIN a.offres o
            JOIN o.reservationOffres r
            WHERE r.statut = 'CONFIRMEE'
            GROUP BY a.id
            ORDER BY COUNT(r.id) DESC
            """)
    List<Agence> findTopAgencesParReservationsConfirmees(Pageable pageable);


}