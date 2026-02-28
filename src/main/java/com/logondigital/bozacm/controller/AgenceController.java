package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.dto.AgenceRequestDTO;
import com.logondigital.bozacm.dto.AgenceResponseDTO;
import com.logondigital.bozacm.dto.PageResponseDTO;
import com.logondigital.bozacm.dto.StatistiquesAgenceDetailDTO;
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
    public ResponseEntity<String> createAgence(@Valid @RequestBody AgenceRequestDTO agence) {
        this.agenceService.createAgence(agence);
        return ResponseEntity.status(200).body("Created !");
    }

    @GetMapping(path = "/get_all")
    public ResponseEntity<List<AgenceResponseDTO>> getAllAgences() {
        return ResponseEntity.status(200).body(this.agenceService.getAllAgences());
    }

    @GetMapping(path = "/get_all_page")
    public ResponseEntity<PageResponseDTO<AgenceResponseDTO>> getAllAgencesPaginated(
            @RequestParam(defaultValue = "0")   int page,
            @RequestParam(defaultValue = "10")  int size,
            @RequestParam(defaultValue = "nom") String sortBy) {
        return ResponseEntity.status(200).body(this.agenceService.getAllAgencesPaginated(page, size, sortBy));
    }

    @GetMapping(path = "/get_by_id/{id}")
    public ResponseEntity<AgenceResponseDTO> getAgenceById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(this.agenceService.getAgenceById(id));
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<String> updateAgence(@Valid @RequestBody AgenceRequestDTO agence,
                                               @PathVariable Integer id) {
        this.agenceService.updateAgence(id, agence);
        return ResponseEntity.status(202).body("Update successfully");
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteAgence(@PathVariable Integer id) {
        this.agenceService.deleteAgence(id);
        return ResponseEntity.status(202).body("Delete successfully");
    }

    @GetMapping("/search/email/{email}")
    public ResponseEntity<AgenceResponseDTO> getAgenceByEmail(@PathVariable String email) {
        return ResponseEntity.status(200).body(this.agenceService.getAgenceByEmail(email));
    }

    @GetMapping("/search/ville/{ville}")
    public ResponseEntity<List<AgenceResponseDTO>> getAgencesByVille(@PathVariable String ville) {
        return ResponseEntity.status(200).body(this.agenceService.getAgencesByVille(ville));
    }
    @GetMapping("/search/{terme}")
    public ResponseEntity<List<AgenceResponseDTO>> rechercher(@PathVariable String terme) {
        return ResponseEntity.status(200).body(agenceService.rechercher(terme));
    }
    @GetMapping("/classement")
    public ResponseEntity<List<StatistiquesAgenceDetailDTO>> getClassementAgences() {
        return ResponseEntity.status(200).body(this.agenceService.getClassementAgences());
    }

    @GetMapping("/statistiques/{id}")
    public ResponseEntity<StatistiquesAgenceDetailDTO> getStatistiquesAgence(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(this.agenceService.getStatistiquesAgence(id));
    }
}