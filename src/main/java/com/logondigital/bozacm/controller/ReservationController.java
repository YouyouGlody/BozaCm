package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.entities.Reservation;
import com.logondigital.bozacm.service.reservation.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor //pour l'injection de dépendances

public class ReservationController {

    private final ReservationService reservationService;


    // ✅ Créer une réservation
    @PostMapping(path = "/create")
    public ResponseEntity<String> createReservation(@RequestBody Reservation reservation) {
        //Créer la réservation
        this.reservationService.createReservation(reservation);
        //Retourne le message
        return ResponseEntity.status(201).body("La Réservation a été créée avec succès !");
    }

    // ✅ Récupérer toutes les réservations
    @GetMapping(path = "/get_all")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.status(200).body(this.reservationService.getAllReservations());
    }

    // ✅ Récupérer une réservation par ID
    @GetMapping(path = "/get_by_id/{idReservation}")
    public ResponseEntity<Reservation> getReservationById(@Valid @PathVariable Integer idReservation) {
        return ResponseEntity.status(200).body(this.reservationService.getReservationById(idReservation));
    }

    // ✅ Mettre à jour une réservation
    @PutMapping(path = "/update/{idReservation}")
    public ResponseEntity<String> updateReservation(@PathVariable Integer idReservation, @RequestBody Reservation reservation) {
        //Modifier la réservation
        this.reservationService.updateReservation(idReservation, reservation);
        //Retourne le message
        return ResponseEntity.status(202)
                .body("La Réservation avec l'ID " + idReservation + " a été modifiée avec succès !");
    }

    // ✅ Supprimer une réservation
    @DeleteMapping(path = "/delete/{idReservation}")
    public ResponseEntity<String> deleteReservation(@PathVariable Integer idReservation) {
        // Récupérer la réservation avant suppression pour le message
        Reservation reservation = reservationService.getReservationById(idReservation);
        // Supprimer la réservation
        this.reservationService.deleteReservation(idReservation);
        // Retourner le message
        return ResponseEntity.status(202)
                .body("La Réservation avec l'ID " + idReservation + " a été supprimée avec succès !");
    }


    // ✅ Supprimer toutes les réservations
    @DeleteMapping(path = "/delete_all")
    public ResponseEntity<String> deleteAllReservations() {
        this.reservationService.deleteAllReservations();
        return ResponseEntity.status(202)
                .body("Toutes les réservations ont été supprimées avec succès !");
    }


}
