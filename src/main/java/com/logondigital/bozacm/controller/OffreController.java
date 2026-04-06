package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.dto.OffreRequestDTO;
import com.logondigital.bozacm.dto.OffreResponseDTO;
import com.logondigital.bozacm.dto.PageResponseDTO;
import com.logondigital.bozacm.dto.RechercheOffreDTO;
import com.logondigital.bozacm.service.Offre.OffreService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/offres")
public class OffreController {

    private final OffreService offreService;

    public OffreController(OffreService offreService) {
        this.offreService = offreService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> createOffre(@Valid @RequestBody OffreRequestDTO offre) {
        this.offreService.createOffre(offre);
        return ResponseEntity.status(200).body("Created !");
    }

    @GetMapping(path = "/get_all")
    public ResponseEntity<List<OffreResponseDTO>> getAllOffres() {
        return ResponseEntity.status(200).body(this.offreService.getAllOffres());
    }

    @GetMapping(path = "/get_all_page")
    public ResponseEntity<PageResponseDTO<OffreResponseDTO>> getAllOffresPaginated(
            @RequestParam(defaultValue = "0")          int page,
            @RequestParam(defaultValue = "10")         int size,
            @RequestParam(defaultValue = "dateDepart") String sortBy) {
        return ResponseEntity.status(200).body(this.offreService.getAllOffresPaginated(page, size, sortBy));
    }

    @GetMapping(path = "/get_by_id/{id}")
    public ResponseEntity<OffreResponseDTO> getOffreById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(this.offreService.getOffreById(id));
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<String> updateOffre(@Valid @RequestBody OffreRequestDTO offre,
                                              @PathVariable Integer id) {
        this.offreService.updateOffre(id, offre);
        return ResponseEntity.status(202).body("Update successfully");
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteOffre(@PathVariable Integer id) {
        this.offreService.deleteOffre(id);
        return ResponseEntity.status(202).body("Delete successfully");
    }

    // Recherche multicritère — fonctionnalité avancée
    @GetMapping("/recherche")
    public ResponseEntity<PageResponseDTO<OffreResponseDTO>> rechercherOffres(
            @RequestParam(required = false) String villeDepart,
            @RequestParam(required = false) String villeArrivee,
            @RequestParam(required = false) Double prixMin,
            @RequestParam(required = false) Double prixMax,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDepart,
            @RequestParam(required = false) Integer agenceId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        RechercheOffreDTO criteres = new RechercheOffreDTO(
                villeDepart, villeArrivee, prixMin, prixMax, dateDepart, agenceId);
        return ResponseEntity.status(200).body(this.offreService.rechercherOffres(criteres, page, size));
    }

    @GetMapping("/search/prix/{min}/{max}")
    public ResponseEntity<List<OffreResponseDTO>> getOffresByPrixRange(
            @PathVariable Double min,
            @PathVariable Double max) {
        return ResponseEntity.status(200).body(this.offreService.getOffresByPrixRange(min, max));
    }

    @GetMapping("/search/agence/{agenceId}")
    public ResponseEntity<PageResponseDTO<OffreResponseDTO>> getOffresByAgence(
            @PathVariable Integer agenceId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.status(200).body(this.offreService.getOffresByAgence(agenceId, page, size));
    }
}