package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.dto.PageResponseDTO;
import com.logondigital.bozacm.dto.ReservationRequestDTO;
import com.logondigital.bozacm.dto.ReservationResponseDTO;
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
    public ResponseEntity<String> createReservation(@RequestBody ReservationRequestDTO reservation) {
        this.reservationService.createReservation(reservation);
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
    public ResponseEntity<String> updateReservation(@RequestBody ReservationRequestDTO reservation, @PathVariable Integer id) {
        this.reservationService.updateReservation(id, reservation);
        return  ResponseEntity.status(202).body("Update successfully");
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteSuccesfully(@PathVariable Integer id) {
        this.reservationService.deleteReservation(id);
        return ResponseEntity.status(202).body("Delete successfully");
    }


    @GetMapping(path = "/paginated")
    public ResponseEntity<PageResponseDTO<ReservationResponseDTO>> getAllReservationsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateReservation") String sortBy) {
        return ResponseEntity.ok(reservationService.getAllReservationsPaginated(page, size, sortBy));
    }

    @GetMapping("/search/statut/{statut}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByStatut(@PathVariable String statut) {
        return ResponseEntity.status(200).body(reservationService.getReservationsByStatut(statut));
    }

    @GetMapping("/search/client/{email}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByClient(@PathVariable String email) {
        return ResponseEntity.status(200).body(reservationService.getReservationsByClient(email));
    }
}