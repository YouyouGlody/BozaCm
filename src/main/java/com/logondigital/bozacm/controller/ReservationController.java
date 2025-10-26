package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.dto.ReservationRequestDTO;
import com.logondigital.bozacm.dto.ReservationResponseDTO;
import com.logondigital.bozacm.entities.Reservation;
import com.logondigital.bozacm.service.reservation.ReservationService;
import jakarta.validation.Valid;
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
    public ResponseEntity<String> createReservation(@RequestBody @Valid ReservationRequestDTO dto ) {
        this.reservationService.createReservation( dto);
        return ResponseEntity.status(200).body("Created !");
    }

    @GetMapping(path = "/get_all")
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        return ResponseEntity.status(200).body(this.reservationService.getAllReservations());
    }

    @GetMapping(path = "/get_by_id/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservation(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(this.reservationService.getReservationById(id));
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<String> updateReservation(@RequestBody @Valid ReservationRequestDTO dto, @PathVariable Integer id) {
        this.reservationService.updateReservation(id, dto);
        return ResponseEntity.status(202).body("Update successfully");
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteSuccesfully(@PathVariable Integer id) {
        this.reservationService.deleteReservation(id);
        return ResponseEntity.status(202).body("Delete successfully");
    }
}




