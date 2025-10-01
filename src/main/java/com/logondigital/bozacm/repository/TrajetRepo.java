package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrajetRepo extends JpaRepository<Trajet, Integer> {

}

