package com.logondigital.bozacm.repository;


import com.logondigital.bozacm.entities.Etape;
import com.logondigital.bozacm.entities.Trajet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EtapeRepo extends JpaRepository<Etape,Integer> {
    Optional<Etape> findByNomEtape(String nomEtape);

    //recuperer toutes les escales d'un trajet grace a trajetid
    @Query("SELECT e FROM Etape e WHERE e.trajet.id = :trajetId AND e.typeEtape = 'ESCALE' ORDER BY e.ordre ASC")
    List<Etape> findEscalesByTrajet(@Param("trajetId") Integer trajetId);
    //paginage / tri
    @Query("SELECT e FROM Etape e WHERE LOWER(e.nomEtape) LIKE LOWER(CONCAT('%', :motCle, '%'))")
    Page<Etape> findByNomEtapeContainingIgnoreCase(@Param("motCle") String motCle, Pageable pageable);
    // rechercher les escales par une ville
    @Query("SELECT e FROM Etape e WHERE LOWER(e.ville) = LOWER(:ville) AND e.typeEtape = 'ESCALE'")
    List<Etape> findEscalesByVille(@Param("ville") String ville);

}
