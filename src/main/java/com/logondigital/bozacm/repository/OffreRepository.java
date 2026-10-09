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
     * Aucun paramètre n'est null : le service remplace chaque critère absent par une valeur
     * « neutre » (motif '%', prix 0 / max, date très ancienne, agence 0). PostgreSQL ne sait pas
     * typer un paramètre null dans « :p IS NULL », ce qui provoquait l'erreur lower(bytea).
     * Les motifs de texte arrivent déjà en minuscules et entourés de '%'.
     */
    @Query("""
            SELECT o FROM Offre o
            JOIN o.trajet t
            JOIN o.agence a
            WHERE LOWER(t.depart)  LIKE :villeDepart
            AND   LOWER(t.arrivee) LIKE :villeArrivee
            AND   (LOWER(o.titre) LIKE :motCle OR LOWER(a.nom) LIKE :motCle
                   OR LOWER(t.depart) LIKE :motCle OR LOWER(t.arrivee) LIKE :motCle)
            AND   o.prix >= :prixMin
            AND   o.prix <= :prixMax
            AND   o.dateDepart >= :dateDepart
            AND   (:agenceId = 0 OR a.id = :agenceId)
            """)
    Page<Offre> rechercherOffres(
            @Param("villeDepart")  String    villeDepart,
            @Param("villeArrivee") String    villeArrivee,
            @Param("motCle")       String    motCle,
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
            JOIN com.logondigital.bozacm.entities.reservation.Reservation r ON r.offre = o
            WHERE r.statutReservation = com.logondigital.bozacm.enums.StatutReservation.CONFIRMEE
            GROUP BY o.id
            ORDER BY COUNT(r.idReservation) DESC
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

    /**
     * Nombre de réservations non annulées sur une offre (places occupées).
     */
    @Query("""
            SELECT COUNT(r) FROM com.logondigital.bozacm.entities.reservation.Reservation r
            WHERE r.offre.id = :offreId
            AND r.statutReservation <> com.logondigital.bozacm.enums.StatutReservation.ANNULEE
            """)
    Long countReservationsActives(@Param("offreId") Integer offreId);

    /**
     * Nombre total de réservations (annulées comprises) rattachées à une offre.
     */
    @Query("SELECT COUNT(r) FROM com.logondigital.bozacm.entities.reservation.Reservation r WHERE r.offre.id = :offreId")
    Long countToutesReservations(@Param("offreId") Integer offreId);

    /**
     * Places encore disponibles sur les offres à venir (départ aujourd'hui ou plus tard).
     */
    @Query("SELECT COALESCE(SUM(o.placesDisponibles), 0) FROM Offre o WHERE o.dateDepart >= :aujourdhui")
    Long sommePlacesDisponiblesAVenir(@Param("aujourdhui") LocalDate aujourdhui);

    /**
     * Nombre d'offres à venir (départ aujourd'hui ou plus tard).
     */
    @Query("SELECT COUNT(o) FROM Offre o WHERE o.dateDepart >= :aujourdhui")
    Long countOffresAVenir(@Param("aujourdhui") LocalDate aujourdhui);
}
