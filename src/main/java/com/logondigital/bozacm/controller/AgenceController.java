package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.dto.AgenceRequestDTO;
import com.logondigital.bozacm.dto.AgenceResponseDTO;
import com.logondigital.bozacm.dto.StatistiquesAgenceDetailDTO;
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
    public ResponseEntity<String> createAgence(@RequestBody AgenceRequestDTO agence) {
        this.agenceService.createAgence(agence);
        return ResponseEntity.status(200).body("Created !");
    }

    @GetMapping(path = "/get_all")
    public ResponseEntity<List<AgenceResponseDTO>> getAllAgences() {
        return ResponseEntity.status(200).body(this.agenceService.getAllAgences());
    }

    @GetMapping(path = "/get_by_id/{id}")
    public ResponseEntity<AgenceResponseDTO> getAgence(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(this.agenceService.getAgenceById(id));
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<String> updateAgence(@RequestBody AgenceRequestDTO agence, @PathVariable Integer id) {
        this.agenceService.updateAgence(id, agence);
        return ResponseEntity.status(202).body("Update successfully");
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteSuccesfully(@PathVariable Integer id) {
        this.agenceService.deleteAgence(id);
        return ResponseEntity.status(202).body("Delete successfully");
    }

    @GetMapping("/search/email/{email}")
    public ResponseEntity<AgenceResponseDTO> getAgenceByEmail(@PathVariable String email) {
        return ResponseEntity.status(200).body(agenceService.getAgenceByEmail(email));
    }

    @GetMapping("/search/ville/{ville}")
    public ResponseEntity<List<AgenceResponseDTO>> getAgencesByVille(@PathVariable String ville) {
        return ResponseEntity.status(200).body(agenceService.getAgencesByVille(ville));
    }

    @GetMapping("/classement")
    public ResponseEntity<List<StatistiquesAgenceDetailDTO>> getClassementAgences() {
        return ResponseEntity.status(200).body(agenceService.getClassementAgences());
    }

    @GetMapping("/statistiques/{id}")
    public ResponseEntity<StatistiquesAgenceDetailDTO> getStatistiquesAgence(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(agenceService.getStatistiquesAgence(id));
    }

}