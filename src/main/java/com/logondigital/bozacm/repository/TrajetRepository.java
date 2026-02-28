package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Trajet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrajetRepository extends JpaRepository<Trajet, Integer> {

    // ─── Recherches simples ────────────────────────────────────────────────────

    /**
     * Recherche un trajet exact par ville de départ et d'arrivée (insensible à la casse).
     * Utile pour éviter les doublons à la création.
     */
    @Query("""
            SELECT t FROM Trajet t
            WHERE LOWER(t.depart) = LOWER(:depart)
            AND   LOWER(t.arrivee) = LOWER(:arrivee)
            """)
    Optional<Trajet> findByDepartAndArriveeIgnoreCase(
            @Param("depart")  String depart,
            @Param("arrivee") String arrivee
    );

    /**
     * Recherche les trajets dont la ville de départ contient le mot-clé (insensible à la casse).
     */
    @Query("SELECT t FROM Trajet t WHERE LOWER(t.depart) LIKE LOWER(CONCAT('%', :motCle, '%'))")
    Page<Trajet> findByDepartContainingIgnoreCase(@Param("motCle") String motCle, Pageable pageable);

    /**
     * Recherche les trajets dont la ville d'arrivée contient le mot-clé (insensible à la casse).
     */
    @Query("SELECT t FROM Trajet t WHERE LOWER(t.arrivee) LIKE LOWER(CONCAT('%', :motCle, '%'))")
    Page<Trajet> findByArriveeContainingIgnoreCase(@Param("motCle") String motCle, Pageable pageable);

    /**
     * Recherche multicritère : départ ET/OU arrivée (insensible à la casse).
     * Si un paramètre est null ou vide, il est ignoré dans le filtre.
     */
    @Query("""
            SELECT t FROM Trajet t
            WHERE (:depart  IS NULL OR LOWER(t.depart)  LIKE LOWER(CONCAT('%', :depart,  '%')))
            AND   (:arrivee IS NULL OR LOWER(t.arrivee) LIKE LOWER(CONCAT('%', :arrivee, '%')))
            """)
    Page<Trajet> findByDepartAndArriveeOptional(
            @Param("depart")  String depart,
            @Param("arrivee") String arrivee,
            Pageable pageable
    );

    /**
     * Récupère tous les trajets qui ont au moins une offre associée.
     */
    @Query("SELECT DISTINCT t FROM Trajet t JOIN t.offres o")
    List<Trajet> findTrajetsAvecOffres();

    /**
     * Récupère les trajets qui n'ont aucune offre associée.
     * Utile pour détecter les trajets orphelins.
     */
    @Query("SELECT t FROM Trajet t WHERE t.offres IS EMPTY")
    List<Trajet> findTrajetsInactifs();

    /**
     * Vérifie si un trajet avec ce départ et cette arrivée existe déjà.
     */
    @Query("""
            SELECT COUNT(t) > 0 FROM Trajet t
            WHERE LOWER(t.depart) = LOWER(:depart)
            AND   LOWER(t.arrivee) = LOWER(:arrivee)
            """)
    boolean existsByDepartAndArriveeIgnoreCase(
            @Param("depart")  String depart,
            @Param("arrivee") String arrivee
    );

    /**
     * Compte le nombre d'offres associées à un trajet.
     */
    @Query("SELECT COUNT(o) FROM Offre o WHERE o.trajet.id = :trajetId")
    Long countOffresByTrajetId(@Param("trajetId") Integer trajetId);

    /**
     * Récupère les trajets les plus populaires (par nombre de réservations confirmées).
     */
    @Query("""
            SELECT t FROM Trajet t
            JOIN t.offres o
            JOIN o.reservationOffres r
            WHERE r.statut = 'CONFIRMEE'
            GROUP BY t.id
            ORDER BY COUNT(r.id) DESC
            """)
    List<Trajet> findTrajetsLesPlusPopulaires(Pageable pageable);
}