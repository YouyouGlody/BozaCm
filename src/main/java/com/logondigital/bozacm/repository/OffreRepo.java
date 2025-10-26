package com.logondigital.bozacm.repository;


import com.logondigital.bozacm.entities.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffreRepo  extends JpaRepository<Offre, Integer> {
    @Query("SELECT DISTINCT o FROM Offre o " +
            "LEFT JOIN FETCH o.agence " +
            "LEFT JOIN FETCH o.trajet")
    List<Offre> findAllWithAgenceAndTrajet();
}

