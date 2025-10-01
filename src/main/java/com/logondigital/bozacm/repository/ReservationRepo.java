package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepo extends JpaRepository<Reservation, Integer> {
}
