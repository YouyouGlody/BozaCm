package com.logondigital.bozacm.repository;


import com.logondigital.bozacm.entities.Etape;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EtapeRepo extends JpaRepository<Etape,Integer> {
    Optional<Etape> findByNomEtape(String nomEtape);

}
