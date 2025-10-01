package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienRepo extends JpaRepository<Client, Integer> {
}
