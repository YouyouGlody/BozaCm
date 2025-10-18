package com.logondigital.bozacm.controller;



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
    public ResponseEntity<String> createTrajet(@RequestBody  @Valid  Trajet trajet) {
        this.trajetService.createTrajet(trajet);
        return ResponseEntity.status(200).body("Created !");
    }

    @GetMapping(path = "/get_all")
    public ResponseEntity<List<Trajet>> getAllTrajets() {
        return ResponseEntity.status(200).body(this.trajetService.getTrajets());
    }

    @GetMapping(path = "/get_by_id/{id}")
    public ResponseEntity<Trajet> getTrajet(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(this.trajetService.getTrajetById(id));
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<String> updateTrajet(@RequestBody @Valid Trajet trajet, @PathVariable Integer id) {
        this.trajetService.updateTrajet(id, trajet);
        return ResponseEntity.status(202).body("Update successfully");
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteSuccesfully(@PathVariable Integer id) {
        this.trajetService.deleteTrajet(id);
        return ResponseEntity.status(202).body("Delete successfully");
    }
}


