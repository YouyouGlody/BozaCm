package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Offre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OffreRepository extends JpaRepository<Offre, Integer> {

    // ─── Recherches par agence ─────────────────────────────────────────────────

    /**
     * Récupère toutes les offres d'une agence, paginées.
     */
    @Query("SELECT o FROM Offre o WHERE o.agence.id = :agenceId")
    Page<Offre> findByAgenceId(@Param("agenceId") Integer agenceId, Pageable pageable);

    /**
     * Récupère toutes les offres d'une agence par son nom (insensible à la casse).
     */
    @Query("SELECT o FROM Offre o WHERE LOWER(o.agence.nom) LIKE LOWER(CONCAT('%', :nomAgence, '%'))")
    List<Offre> findByAgenceNomIgnoreCase(@Param("nomAgence") String nomAgence);

    // ─── Recherches par trajet ─────────────────────────────────────────────────

    /**
     * Récupère toutes les offres pour un trajet donné, paginées.
     */
    @Query("SELECT o FROM Offre o WHERE o.trajet.id = :trajetId")
    Page<Offre> findByTrajetId(@Param("trajetId") Integer trajetId, Pageable pageable);

    // ─── Recherche par titre ───────────────────────────────────────────────────

    /**
     * Recherche les offres dont le titre contient un mot-clé (insensible à la casse).
     */
    @Query("SELECT o FROM Offre o WHERE LOWER(o.titre) LIKE LOWER(CONCAT('%', :motCle, '%'))")
    Page<Offre> findByTitreContainingIgnoreCase(@Param("motCle") String motCle, Pageable pageable);

    // ─── Recherche par prix ────────────────────────────────────────────────────

    /**
     * Récupère les offres dans une fourchette de prix.
     */
    @Query("SELECT o FROM Offre o WHERE o.prix BETWEEN :prixMin AND :prixMax ORDER BY o.prix ASC")
    List<Offre> findByPrixBetween(
            @Param("prixMin") Double prixMin,
            @Param("prixMax") Double prixMax
    );

    // ─── Recherche par date ────────────────────────────────────────────────────

    /**
     * Récupère les offres à partir d'une date de départ donnée.
     */
    @Query("SELECT o FROM Offre o WHERE o.dateDepart >= :dateDepart ORDER BY o.dateDepart ASC")
    List<Offre> findByDateDepartAfterOrEqual(@Param("dateDepart") LocalDate dateDepart);

    /**
     * Récupère les offres dont la date de départ est dans une plage donnée.
     */
    @Query("SELECT o FROM Offre o WHERE o.dateDepart BETWEEN :debut AND :fin ORDER BY o.dateDepart ASC")
    List<Offre> findByDateDepartBetween(
            @Param("debut") LocalDate debut,
            @Param("fin")   LocalDate fin
    );

    // ─── Recherche multicritère (RechercheOffreDTO) ───────────────────────────

    /**
     * Recherche multicritère complète correspondant à RechercheOffreDTO.
     * Chaque paramètre est optionnel : s'il est null, il est ignoré dans le filtre.
     * La recherche sur les villes est insensible à la casse.
     */
    @Query("""
            SELECT o FROM Offre o
            JOIN o.trajet t
            JOIN o.agence a
            WHERE (:villeDepart  IS NULL OR LOWER(t.depart)  LIKE LOWER(CONCAT('%', :villeDepart,  '%')))
            AND   (:villeArrivee IS NULL OR LOWER(t.arrivee) LIKE LOWER(CONCAT('%', :villeArrivee, '%')))
            AND   (:prixMin      IS NULL OR o.prix >= :prixMin)
            AND   (:prixMax      IS NULL OR o.prix <= :prixMax)
            AND   (:dateDepart   IS NULL OR o.dateDepart >= :dateDepart)
            AND   (:agenceId     IS NULL OR a.id = :agenceId)
            ORDER BY o.dateDepart ASC, o.prix ASC
            """)
    Page<Offre> rechercherOffres(
            @Param("villeDepart")  String    villeDepart,
            @Param("villeArrivee") String    villeArrivee,
            @Param("prixMin")      Double    prixMin,
            @Param("prixMax")      Double    prixMax,
            @Param("dateDepart")   LocalDate dateDepart,
            @Param("agenceId")     Integer   agenceId,
            Pageable pageable
    );

    // ─── Statistiques ──────────────────────────────────────────────────────────

    /**
     * Récupère les offres les plus réservées (triées par nombre de réservations confirmées).
     */
    @Query("""
            SELECT o FROM Offre o
            JOIN o.reservationOffres r
            WHERE r.statut = 'CONFIRMEE'
            GROUP BY o.id
            ORDER BY COUNT(r.id) DESC
            """)
    List<Offre> findOffreLesPlusReservees(Pageable pageable);

    /**
     * Prix moyen de toutes les offres d'une agence.
     */
    @Query("SELECT AVG(o.prix) FROM Offre o WHERE o.agence.id = :agenceId")
    Double findPrixMoyenParAgence(@Param("agenceId") Integer agenceId);

    /**
     * Compte le nombre d'offres futures (date de départ > aujourd'hui).
     */
    @Query("SELECT COUNT(o) FROM Offre o WHERE o.dateDepart > :aujourd_hui")
    Long countOffresActives(@Param("aujourd_hui") LocalDate aujourdhui);
}