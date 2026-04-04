package com.logondigital.bozacm.repository;


import com.logondigital.bozacm.entities.Trajet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrajetRepo extends JpaRepository<Trajet,Integer> {
    Optional<Trajet> findById(@Param("id") Integer id);

    @Query("SELECT t FROM Trajet t WHERE LOWER(t.villeDepart) LIKE LOWER(CONCAT('%', :motCle, '%'))")
    Page<Trajet> findByVilleDepartContainingIgnoreCase(@Param("motCle") String motCle, Pageable pageable);

}
