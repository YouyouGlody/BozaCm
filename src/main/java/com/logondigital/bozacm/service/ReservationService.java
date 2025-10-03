package com.logondigital.bozacm.service;

import com.logondigital.bozacm.entities.Reservation;

import java.util.List;

public interface ReservationService {

    // 1. Ajouter une nouvelle réservation
    void createReservation(Reservation reservation);

    // 2. Récupérer une réservation par son ID
    Reservation getReservationById(Integer idReservation);

    // 3. Récupérer toutes les réservations
    List<Reservation> getAllReservations();

    // 4. Mettre à jour les informations de la réservation
    void updateReservation(Integer idReservation, Reservation reservation);

    // 5. Supprimer une réservation par son ID
    void deleteReservation(Integer idReservation);

    // 6. Supprimer toutes les réservations
    void deleteAllReservations();

}
