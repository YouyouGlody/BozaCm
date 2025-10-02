package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.service.Offre.OffreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/offres")
public class OffreController {
    private final OffreService offreService;

    public OffreController(OffreService offreService) {
        this.offreService = offreService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> createOffre(@RequestBody Offre offre) {
        this.offreService.createOffre(offre);
        return ResponseEntity.status(200).body("Created !");
    }

    @GetMapping(path = "/get_all")
    public ResponseEntity<List<Offre>> getAllOffres() {
        return ResponseEntity.status(200).body(this.offreService.getOffres());
    }

    @GetMapping(path = "/get_by_id/{id}")
    public ResponseEntity<Offre> getOffre(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(this.offreService.getOffreById(id));
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<String> updateOffre(@RequestBody Offre offre, @PathVariable Integer id) {
        this.offreService.updateOffre(id, offre);
        return ResponseEntity.status(202).body("Update successfully");
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteSuccesfully(@PathVariable Integer id) {
        this.offreService.deleteOffre(id);
        return ResponseEntity.status(202).body("Delete successfully");
    }
}



