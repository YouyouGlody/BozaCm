package com.logondigital.bozacm.service.reservation;


import com.logondigital.bozacm.entities.Reservation;
import com.logondigital.bozacm.repository.ReservationRepo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepo reservationRepo;

    public ReservationServiceImpl(ReservationRepo reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    @Override
    public String createReservation(Reservation reservation) {
        reservation.setCreatedAt(new Date());
        this.reservationRepo.save(reservation);
        return "Reservation created";
    }

    @Override
    public List<Reservation> getReservations() {
        return this.reservationRepo.findAll();
    }

    @Override
    public Reservation getReservationById(Integer reservationId) {
        return this.reservationRepo.findById(reservationId).get();
    }

    @Override
    public String updateReservation(Integer reservationId, Reservation reservation) {
        Reservation reservationToUpdate = this.reservationRepo.findById(reservationId).get();
        reservationToUpdate.setName(reservation.getName());
        reservationToUpdate.setUpdatedAt(new Date());
        this.reservationRepo.saveAndFlush(reservationToUpdate);

        return "Reservation updated with succes";
    }

    @Override
    public String deleteReservation(Integer reservationId) {
        this.reservationRepo.deleteById(reservationId);
        return "";
    }

    @Override
    public void CreateReservation(Reservation reservation) {

    }
}

