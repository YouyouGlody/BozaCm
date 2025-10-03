package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Billet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BilletRepo extends JpaRepository<Billet, Integer> {

}
