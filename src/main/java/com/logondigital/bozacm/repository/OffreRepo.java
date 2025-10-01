package com.logondigital.bozacm.repository;


import com.logondigital.bozacm.entities.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffreRepo  extends JpaRepository<Offre, Integer> {
}

