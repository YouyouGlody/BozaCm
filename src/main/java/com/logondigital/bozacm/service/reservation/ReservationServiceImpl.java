package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.entities.Reservation;
import com.logondigital.bozacm.repository.ReservationRepo;
import com.logondigital.bozacm.service.ReservationService;

import java.util.Date;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepo reservationRepo;

    // Injection de dépendance via le constructeur
    public ReservationServiceImpl(ReservationRepo reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    // 1. Ajouter une nouvelle réservation
    @Override
    public void createReservation(Reservation reservation) {
        reservation.setCreatedAt(new Date()); // Définir la date de création
        this.reservationRepo.save(reservation);
    }

    // 2. Récupérer une réservation par son ID
    @Override
    public Reservation getReservationById(Integer idReservation) {
        return this.reservationRepo.findById(idReservation).get();
    }

    // 3. Récupérer toutes les réservations
    @Override
    public List<Reservation> getAllReservations() {
        return this.reservationRepo.findAll();
    }

    // 4. Mettre à jour les informations de la réservation
    @Override
    public void updateReservation(Integer idReservation, Reservation reservation) {
        Reservation reservationToUpdate = this.reservationRepo.findById(idReservation).get();
        reservationToUpdate.setDateReservation(reservation.getDateReservation());
        reservationToUpdate.setStatutReservation(reservation.getStatutReservation());
        reservationToUpdate.setUpdatedAt(new Date());
        this.reservationRepo.saveAndFlush(reservationToUpdate);

    }

    @Override
    public void deleteReservation(Integer idReservation) {
        this.reservationRepo.deleteById(idReservation);

    }

    @Override
    public void deleteAllReservations() {
        this.reservationRepo.deleteAll();

    }
}
