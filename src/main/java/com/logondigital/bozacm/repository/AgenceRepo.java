package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Agence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgenceRepo  extends JpaRepository<Agence, Integer> {
    Optional<Agence> findByEmail(String email);
}

