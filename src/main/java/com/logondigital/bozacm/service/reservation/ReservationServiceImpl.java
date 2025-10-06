package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.entities.Reservation;
import com.logondigital.bozacm.entities.Trajet;
import com.logondigital.bozacm.exception.DatabaseException;
import com.logondigital.bozacm.exception.DuplicateResourceException;
import com.logondigital.bozacm.exception.InvalidRequestException;
import com.logondigital.bozacm.exception.ResourceNotFoundException;
import com.logondigital.bozacm.repository.OffreRepo;
import com.logondigital.bozacm.repository.ReservationRepo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepo reservationRepo;
    private final OffreRepo offreRepo;

    public ReservationServiceImpl(ReservationRepo reservationRepo, OffreRepo offreRepo) {
        this.reservationRepo = reservationRepo;
        this.offreRepo = offreRepo;
    }

    @Override
    public String createReservation(Reservation reservation) {

        // Validation des champs obligatoires
        if (reservation.getNomClient() == null || reservation.getNomClient().isBlank()) {
            throw new InvalidRequestException("Le nom du client est obligatoire !");
        }

        if (reservation.getEmailClient() == null || !reservation.getEmailClient().contains("@")) {
            throw new InvalidRequestException("L'email du client est invalide !");
        }

        // Vérifie que l'offre existe
        Offre offre = offreRepo.findById(reservation.getOffre().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable !"));

        // Vérifie doublon (même client pour la même offre)
        if (reservationRepo.existsByEmailClientAndOffre(reservation.getEmailClient(), offre)) {
            throw new DuplicateResourceException("Le client a déjà réservé cette offre !");
        }

        reservation.setOffre(offre);
        reservation.setCreatedAt(new Date());

        try {
            this.reservationRepo.save(reservation);
            return "Réservation créée avec succès !";
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors de la création de la réservation");
        }
    }

    @Override
    public List<Reservation> getReservations() {
        return this. reservationRepo.findAll();
    }

    @Override
    public Reservation getReservationById(Integer id) {
        return this.reservationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation introuvable"));
    }

    @Override
    public String updateReservation(Integer id, Reservation reservation) {
        Reservation reservationToUpdate = this.reservationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation introuvable"));

        reservationToUpdate.setNomClient(reservation.getNomClient());
        reservationToUpdate.setEmailClient(reservation.getEmailClient());
        reservationToUpdate.setUpdatedAt(new Date());

        try {
            this.reservationRepo.saveAndFlush(reservationToUpdate);
            return "Réservation mise à jour avec succès !";
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors de la mise à jour de la réservation");
        }
    }

    @Override
    public String deleteReservation(Integer id) {

        Reservation reservationToDelete = this.reservationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Le trajet  n’existe pas."));
        this.reservationRepo.deleteById(id);
        return "";
    }

    @Override
    public void CreateReservation(Reservation reservation) {

    }
}
