package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Integer> {
}

