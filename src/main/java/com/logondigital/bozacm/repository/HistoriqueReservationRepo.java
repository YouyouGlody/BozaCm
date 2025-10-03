package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.HistoriqueReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueReservationRepo extends JpaRepository<HistoriqueReservation, Integer> {

    // ❌ Ne pas activer tant que la relation avec Client n'est pas faite
//    List<HistoriqueReservation> findByClientId(Integer idClient);
}
