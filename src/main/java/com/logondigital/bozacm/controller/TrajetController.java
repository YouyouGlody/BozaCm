package com.logondigital.bozacm.controller;



import com.logondigital.bozacm.dto.TrajetRequestDTO;
import com.logondigital.bozacm.dto.TrajetResponseDTO;
import com.logondigital.bozacm.entities.Trajet;
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
    public ResponseEntity<String> createTrajet(@RequestBody  @Valid TrajetRequestDTO dto) {
        this.trajetService.createTrajet(dto);
        return ResponseEntity.status(200).body("Created !");
    }

    @GetMapping(path = "/get_all")
    public ResponseEntity<List<TrajetResponseDTO>> getAllTrajets() {
        return ResponseEntity.status(200).body(this.trajetService.getAllTrajets());
    }

    @GetMapping(path = "/get_by_id/{id}")
    public ResponseEntity<TrajetResponseDTO> getTrajet(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(this.trajetService.getTrajetById(id));
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<String> updateTrajet(@RequestBody @Valid TrajetRequestDTO dto, @PathVariable Integer id) {
        this.trajetService.updateTrajet(id,dto);
        return ResponseEntity.status(202).body("Update successfully");
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteSuccesfully(@PathVariable Integer id) {
        this.trajetService.deleteTrajet(id);
        return ResponseEntity.status(202).body("Delete successfully");
    }

    @GetMapping("/search/depart/{depart}")
    public ResponseEntity<List<TrajetResponseDTO>> getTrajetsByDepart(@PathVariable String depart) {
        return ResponseEntity.status(200).body(trajetService.getTrajetsByDepart(depart));
    }

    @GetMapping("/search/route")
    public ResponseEntity<List<TrajetResponseDTO>> getTrajetsByRoute(
            @RequestParam String depart,
            @RequestParam String arrivee) {
        return ResponseEntity.status(200).body(trajetService.getTrajetsByRoute(depart, arrivee));
    }
}


