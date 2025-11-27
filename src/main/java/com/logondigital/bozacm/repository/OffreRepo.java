package com.logondigital.bozacm.repository;


import com.logondigital.bozacm.entities.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OffreRepo  extends JpaRepository<Offre, Integer> {
    @Query("SELECT DISTINCT o FROM Offre o " +
            "LEFT JOIN FETCH o.agence " +
            "LEFT JOIN FETCH o.trajet")
    List<Offre> findAllWithAgenceAndTrajet();

    @Query("SELECT o FROM Offre o " +
            "WHERE (:villeDepart IS NULL OR o.trajet.depart = :villeDepart) " +
            "AND (:villeArrivee IS NULL OR o.trajet.arrivee = :villeArrivee) " +
            "AND (:prixMin IS NULL OR o.prix >= :prixMin) " +
            "AND (:prixMax IS NULL OR o.prix <= :prixMax) " +
            "AND (:dateDepart IS NULL OR o.dateDepart >= :dateDepart) " +
            "AND (:agenceId IS NULL OR o.agence.id = :agenceId)")
    List<Offre> rechercherOffres(
            @Param("villeDepart") String villeDepart,
            @Param("villeArrivee") String villeArrivee,
            @Param("prixMin") Double prixMin,
            @Param("prixMax") Double prixMax,
            @Param("dateDepart") Date dateDepart,
            @Param("agenceId") Integer agenceId
    );
    @Query("SELECT o FROM Offre o WHERE o.prix BETWEEN :prixMin AND :prixMax")
    List<Offre> findByPrixBetween(@Param("prixMin") Double prixMin, @Param("prixMax") Double prixMax);


    @Query("SELECT o FROM Offre o WHERE o.agence.id = :agenceId")
    List<Offre> findByAgenceId(@Param("agenceId") Integer agenceId);


    // Statistiques complètes par agence
    @Query("SELECT o.agence.id, o.agence.nom, COUNT(o), AVG(o.prix), MIN(o.prix), MAX(o.prix) " +
            "FROM Offre o " +
            "GROUP BY o.agence.id, o.agence.nom " +
            "ORDER BY COUNT(o) DESC")
    List<Object[]> getStatistiquesParAgence();

    // Nombre total d'offres par agence
    @Query("SELECT COUNT(o) FROM Offre o WHERE o.agence.id = :agenceId")
    Long countOffresByAgenceId(@Param("agenceId") Integer agenceId);

}

