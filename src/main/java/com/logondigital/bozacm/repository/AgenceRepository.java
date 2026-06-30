package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.DTO.StatistiquesAgenceDetailDTO;
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

    @Query("SELECT a FROM Agence a WHERE LOWER(a.nom) = LOWER(:nom)")
    Optional<Agence> findByNomIgnoreCase(@Param("nom") String nom);

    @Query("SELECT COUNT(a) > 0 FROM Agence a WHERE LOWER(a.email) = LOWER(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);

    @Query("SELECT a FROM Agence a WHERE LOWER(a.email) = LOWER(:email)")
    Optional<Agence> findByEmailIgnoreCase(@Param("email") String email);

    @Query("SELECT a FROM Agence a WHERE LOWER(a.nom) LIKE LOWER(CONCAT('%', :motCle, '%'))")
    Page<Agence> findByNomContainingIgnoreCase(@Param("motCle") String motCle, Pageable pageable);

    @Query("SELECT a FROM Agence a WHERE LOWER(a.adresse) LIKE LOWER(CONCAT('%', :ville, '%'))")
    List<Agence> findByVilleIgnoreCase(@Param("ville") String ville);

    @Query("SELECT a FROM Agence a WHERE a.telephone = :telephone")
    Optional<Agence> findByTelephone(@Param("telephone") String telephone);

    @Query("SELECT a FROM Agence a WHERE LOWER(a.nom) LIKE LOWER(CONCAT('%', :terme, '%')) OR LOWER(a.adresse) LIKE LOWER(CONCAT('%', :terme, '%')) OR LOWER(a.email) LIKE LOWER(CONCAT('%', :terme, '%'))")
    List<Agence> rechercher(@Param("terme") String terme);

    // ─── Statistiques ──────────────────────────────────────────────────────────

    @Query("SELECT DISTINCT a FROM Agence a JOIN a.offres o")
    List<Agence> findAgencesAvecOffres();

    /**
     * Statistiques détaillées de toutes les agences, triées par chiffre d'affaires.
     * Les réservations sont jointes via l'offre (Reservation pointe vers Offre).
     */
    @Query("""
            SELECT new com.logondigital.bozacm.DTO.StatistiquesAgenceDetailDTO(
                a.id,
                a.nom,
                a.email,
                a.telephone,
                a.adresse,
                COUNT(DISTINCT o.id),
                AVG(o.prix),
                MIN(o.prix),
                MAX(o.prix),
                COUNT(r.idReservation),
                SUM(CASE WHEN r.statutReservation = com.logondigital.bozacm.enums.StatutReservation.CONFIRMEE THEN 1 ELSE 0 END),
                CASE WHEN COUNT(r.idReservation) > 0
                     THEN (SUM(CASE WHEN r.statutReservation = com.logondigital.bozacm.enums.StatutReservation.CONFIRMEE THEN 1.0 ELSE 0.0 END) / COUNT(r.idReservation)) * 100
                     ELSE 0.0 END,
                SUM(CASE WHEN r.statutReservation = com.logondigital.bozacm.enums.StatutReservation.CONFIRMEE THEN o.prix ELSE 0.0 END),
                0
            )
            FROM Agence a
            LEFT JOIN a.offres o
            LEFT JOIN com.logondigital.bozacm.entities.reservation.Reservation r ON r.offre = o
            GROUP BY a.id, a.nom, a.email, a.telephone, a.adresse
            ORDER BY SUM(CASE WHEN r.statutReservation = com.logondigital.bozacm.enums.StatutReservation.CONFIRMEE THEN o.prix ELSE 0.0 END) DESC
            """)
    List<StatistiquesAgenceDetailDTO> findStatistiquesDetailleesToutesAgences();

    /**
     * Statistiques détaillées d'une seule agence.
     */
    @Query("""
            SELECT new com.logondigital.bozacm.DTO.StatistiquesAgenceDetailDTO(
                a.id,
                a.nom,
                a.email,
                a.telephone,
                a.adresse,
                COUNT(DISTINCT o.id),
                AVG(o.prix),
                MIN(o.prix),
                MAX(o.prix),
                COUNT(r.idReservation),
                SUM(CASE WHEN r.statutReservation = com.logondigital.bozacm.enums.StatutReservation.CONFIRMEE THEN 1 ELSE 0 END),
                CASE WHEN COUNT(r.idReservation) > 0
                     THEN (SUM(CASE WHEN r.statutReservation = com.logondigital.bozacm.enums.StatutReservation.CONFIRMEE THEN 1.0 ELSE 0.0 END) / COUNT(r.idReservation)) * 100
                     ELSE 0.0 END,
                SUM(CASE WHEN r.statutReservation = com.logondigital.bozacm.enums.StatutReservation.CONFIRMEE THEN o.prix ELSE 0.0 END),
                0
            )
            FROM Agence a
            LEFT JOIN a.offres o
            LEFT JOIN com.logondigital.bozacm.entities.reservation.Reservation r ON r.offre = o
            WHERE a.id = :agenceId
            GROUP BY a.id, a.nom, a.email, a.telephone, a.adresse
            """)
    Optional<StatistiquesAgenceDetailDTO> findStatistiquesParAgenceId(@Param("agenceId") Integer agenceId);

    @Query("SELECT COUNT(o) FROM Offre o WHERE o.agence.id = :agenceId")
    Long countOffresByAgenceId(@Param("agenceId") Integer agenceId);

    /**
     * Top N agences par nombre de réservations confirmées.
     */
    @Query("""
            SELECT a FROM Agence a
            JOIN a.offres o
            JOIN com.logondigital.bozacm.entities.reservation.Reservation r ON r.offre = o
            WHERE r.statutReservation = com.logondigital.bozacm.enums.StatutReservation.CONFIRMEE
            GROUP BY a.id
            ORDER BY COUNT(r.idReservation) DESC
            """)
    List<Agence> findTopAgencesParReservationsConfirmees(Pageable pageable);
}