package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Agence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenceRepo  extends JpaRepository<Agence, Integer> {
}

