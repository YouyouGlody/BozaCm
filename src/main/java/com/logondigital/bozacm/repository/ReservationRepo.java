package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Integer> {
    boolean existsByEmailClientAndOffre(String emailClient, Offre offre);
    Optional<Reservation> findById(Integer id);

}

