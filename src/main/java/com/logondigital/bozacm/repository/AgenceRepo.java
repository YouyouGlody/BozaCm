package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Agence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgenceRepo  extends JpaRepository<Agence, Integer> {


    @Query("SELECT a FROM Agence a WHERE a.email = :email")
    Optional<Agence> findByEmail(@Param("email") String email);


    @Query("SELECT a FROM Agence a WHERE a.adresse LIKE CONCAT('%', :ville, '%')")
    List<Agence> findByVille(@Param("ville") String ville);
}

