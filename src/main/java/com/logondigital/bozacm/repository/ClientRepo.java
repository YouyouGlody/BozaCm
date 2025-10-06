package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientRepo extends JpaRepository<Client, Integer> {

    /**
     * Vérifie si un client existe avec cette adresse email
     */
    boolean existsByEmail(String email);



}
