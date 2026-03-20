package com.logondigital.bozacm.repository;


import com.logondigital.bozacm.entities.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrajetRepo extends JpaRepository<Trajet,Integer> {
    Optional<Trajet> findById(@Param("id") Integer id);
Optional<Trajet> findByOffre_offreId(Integer offre_id);


}
