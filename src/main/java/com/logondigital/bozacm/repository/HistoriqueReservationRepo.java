package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.HistoriqueReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueReservationRepo extends JpaRepository<HistoriqueReservation, Integer> {

   List<HistoriqueReservation> findByClient_IdClient(Integer idClient);
}
