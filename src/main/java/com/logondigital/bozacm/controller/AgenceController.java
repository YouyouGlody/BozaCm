package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.dto.AgenceRequestDTO;
import com.logondigital.bozacm.dto.AgenceResponseDTO;
import com.logondigital.bozacm.entities.Agence;
import com.logondigital.bozacm.service.agence.AgenceService;
import jakarta.validation.Valid;
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
    public ResponseEntity<String> createAgence(@RequestBody @Valid AgenceRequestDTO dto) {
        this.agenceService.createAgence(dto);
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
    public ResponseEntity<String> updateAgence(@RequestBody  @ Valid AgenceRequestDTO dto, @PathVariable Integer id) {
        this.agenceService.updateAgence(id, dto);
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
}

