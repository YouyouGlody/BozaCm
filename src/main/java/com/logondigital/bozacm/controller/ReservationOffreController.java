package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.dto.PageResponseDTO;
import com.logondigital.bozacm.dto.ReservationRequestDTO;
import com.logondigital.bozacm.dto.ReservationResponseDTO;
import com.logondigital.bozacm.service.reservation.ReservationOffreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/reservations-offres")
public class ReservationOffreController {
    private final ReservationOffreService reservationOffreService;

    public ReservationOffreController(ReservationOffreService reservationOffreService) {
        this.reservationOffreService = reservationOffreService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> createReservation(@RequestBody ReservationRequestDTO reservation) {
        this.reservationOffreService.createReservation(reservation);
        return ResponseEntity.status(200).body("Created !");
    }

    @GetMapping(path = "/get_all")
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        return ResponseEntity.status(200).body(this.reservationOffreService.getAllReservations());
    }

    @GetMapping(path = "/get_by_id/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservation(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(this.reservationOffreService.getReservationById(id));
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<String> updateReservation(@RequestBody ReservationRequestDTO reservation, @PathVariable Integer id) {
        this.reservationOffreService.updateReservation(id, reservation);
        return  ResponseEntity.status(202).body("Update successfully");
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteSuccesfully(@PathVariable Integer id) {
        this.reservationOffreService.deleteReservation(id);
        return ResponseEntity.status(202).body("Delete successfully");
    }


    @GetMapping(path = "/paginated")
    public ResponseEntity<PageResponseDTO<ReservationResponseDTO>> getAllReservationsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateReservation") String sortBy) {
        return ResponseEntity.ok(reservationOffreService.getAllReservationsPaginated(page, size, sortBy));
    }

    @GetMapping("/search/statut/{statut}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByStatut(@PathVariable String statut) {
        return ResponseEntity.status(200).body(reservationOffreService.getReservationsByStatut(statut));
    }

    @GetMapping("/search/client/{email}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByClient(@PathVariable String email) {
        return ResponseEntity.status(200).body(reservationOffreService.getReservationsByClient(email));
    }
}