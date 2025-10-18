package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.entities.Reservation;
import com.logondigital.bozacm.exception.ResourceNotFoundException;
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
    public void createReservation(Reservation reservation) {
        reservation.setCreatedAt(new Date());
        reservationRepo.save(reservation);
    }

    @Override
    public List<Reservation> getReservations() {
        return reservationRepo.findAll();
    }

    @Override
    public Reservation getReservationById(Integer reservationId) {
        return reservationRepo.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("La réservation n'existe pas."));
    }

    @Override
    public void updateReservation(Integer reservationId, Reservation reservation) {
        Reservation resToUpdate = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("La réservation n'existe pas."));
        resToUpdate.setNomClient(reservation.getNomClient());
        resToUpdate.setEmailClient(reservation.getEmailClient());
        resToUpdate.setDateReservation(reservation.getDateReservation());
        resToUpdate.setStatut(reservation.getStatut());
        resToUpdate.setOffre(reservation.getOffre());
        resToUpdate.setUpdatedAt(new Date());
        reservationRepo.saveAndFlush(resToUpdate);
    }

    @Override
    public void deleteReservation(Integer reservationId) {
        reservationRepo.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("La réservation n'existe pas."));
        reservationRepo.deleteById(reservationId);
    }
}