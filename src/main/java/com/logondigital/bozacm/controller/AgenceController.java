package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.entities.Agence;
import com.logondigital.bozacm.service.agence.AgenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/agences")
public class AgenceController {
    private final AgenceService agenceService;

    public AgenceController(AgenceService agenceService) {
        this.agenceService = agenceService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> createAgence(@RequestBody Agence agence) {
        this.agenceService.createAgence(agence);
        return ResponseEntity.status(200).body("Created !");
    }

    @GetMapping(path = "/get_all")
    public ResponseEntity<List<Agence>> getAllAgences() {
        return ResponseEntity.status(200).body(this.agenceService.getAgences());
    }

    @GetMapping(path = "/get_by_id/{id}")
    public ResponseEntity<Agence> getAgence(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(this.agenceService.getAgenceById(id));
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<String> updateAgence(@RequestBody Agence agence, @PathVariable Integer id) {
        this.agenceService.updateAgence(id, agence);
        return ResponseEntity.status(202).body("Update successfully");
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteSuccesfully(@PathVariable Integer id) {
        this.agenceService.deleteAgence(id);
        return ResponseEntity.status(202).body("Delete successfully");
    }
}

