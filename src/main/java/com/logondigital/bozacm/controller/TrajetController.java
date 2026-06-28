package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.DTO.PageResponseDTO;
import com.logondigital.bozacm.DTO.TrajetRequestDTO;
import com.logondigital.bozacm.DTO.TrajetResponseDTO;
import com.logondigital.bozacm.service.trajet.TrajetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/trajets")
public class TrajetController {

    private final TrajetService trajetService;

    public TrajetController(TrajetService trajetService) {
        this.trajetService = trajetService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> createTrajet(@Valid @RequestBody TrajetRequestDTO trajet) {
        this.trajetService.createTrajet(trajet);
        return ResponseEntity.status(200).body("Created !");
    }

    @GetMapping(path = "/get_all")
    public ResponseEntity<List<TrajetResponseDTO>> getAllTrajets() {
        return ResponseEntity.status(200).body(this.trajetService.getAllTrajets());
    }

    @GetMapping(path = "/get_all_page")
    public ResponseEntity<PageResponseDTO<TrajetResponseDTO>> getAllTrajetsPaginated(
            @RequestParam(defaultValue = "0")      int page,
            @RequestParam(defaultValue = "10")     int size,
            @RequestParam(defaultValue = "depart") String sortBy) {
        return ResponseEntity.status(200).body(this.trajetService.getAllTrajetsPaginated(page, size, sortBy));
    }

    @GetMapping(path = "/get_by_id/{id}")
    public ResponseEntity<TrajetResponseDTO> getTrajetById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(this.trajetService.getTrajetById(id));
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<String> updateTrajet(@Valid @RequestBody TrajetRequestDTO trajet,
                                               @PathVariable Integer id) {
        this.trajetService.updateTrajet(id, trajet);
        return ResponseEntity.status(202).body("Update successfully");
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteTrajet(@PathVariable Integer id) {
        this.trajetService.deleteTrajet(id);
        return ResponseEntity.status(202).body("Delete successfully");
    }

    @GetMapping("/search/depart/{depart}")
    public ResponseEntity<List<TrajetResponseDTO>> getTrajetsByDepart(@PathVariable String depart) {
        return ResponseEntity.status(200).body(this.trajetService.getTrajetsByDepart(depart));
    }

    @GetMapping("/search/route/{depart}/{arrivee}")
    public ResponseEntity<List<TrajetResponseDTO>> getTrajetsByRoute(
            @PathVariable String depart,
            @PathVariable String arrivee) {
        return ResponseEntity.status(200).body(this.trajetService.getTrajetsByRoute(depart, arrivee));
    }

    @GetMapping("/search/{terme}")
    public ResponseEntity<List<TrajetResponseDTO>> rechercher(@PathVariable String terme) {
        return ResponseEntity.status(200).body(this.trajetService.rechercher(terme));
    }
}