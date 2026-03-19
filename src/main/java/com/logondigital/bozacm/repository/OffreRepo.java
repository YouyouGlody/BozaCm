package com.logondigital.bozacm.repository;


import com.logondigital.bozacm.entities.Offre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OffreRepo extends JpaRepository<Offre,Integer> {
    Optional<Offre> findByNomOffre(String nomOffre);
}
