package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.entities.Reservation;

import java.util.List;

public interface ReservationService {
    String createReservation(Reservation reservation);

    List<Reservation> getReservations();

    Reservation getReservationById(Integer reservationId);

    String updateReservation(Integer reservationId, Reservation reservation);

    String deleteReservation(Integer reservationId);

    void CreateReservation(Reservation reservation);
}

