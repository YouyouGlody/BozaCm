package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.entities.Reservation;
import com.logondigital.bozacm.service.reservation.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> createReservation(@RequestBody Reservation reservation) {
        this.reservationService.createReservation(reservation);
        return ResponseEntity.status(200).body("Created !");
    }

    @GetMapping(path = "/get_all")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.status(200).body(this.reservationService.getReservations());
    }

    @GetMapping(path = "/get_by_id/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(this.reservationService.getReservationById(id));
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<String> updateReservation(@RequestBody Reservation reservation, @PathVariable Integer id) {
        this.reservationService.updateReservation(id, reservation);
        return ResponseEntity.status(202).body("Update successfully");
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteSuccesfully(@PathVariable Integer id) {
        this.reservationService.deleteReservation(id);
        return ResponseEntity.status(202).body("Delete successfully");
    }
}




