package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrajetRepo extends JpaRepository<Trajet, Integer> {



    @Query("SELECT t FROM Trajet t WHERE t.depart = :depart")
    List<Trajet> findByDepart(@Param("depart") String depart);


    @Query("SELECT t FROM Trajet t WHERE t.depart = :depart AND t.arrivee = :arrivee")
    List<Trajet> findByDepartAndArrivee(@Param("depart") String depart, @Param("arrivee") String arrivee);
}

