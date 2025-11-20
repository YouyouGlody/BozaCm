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

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/offres")
public class OffreController {
    private final OffreService offreService;

    public OffreController(OffreService offreService) {
        this.offreService = offreService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> createOffre(@RequestBody @Valid OffreRequestDTO dto) {
        this.offreService.createOffre(dto);
        return ResponseEntity.status(200).body("Created !");
    }

    @GetMapping(path = "/get_all")
    public ResponseEntity<List<OffreResponseDTO>> getAllOffres() {
        return ResponseEntity.status(200).body(this.offreService.getAllOffres());
    }

    @GetMapping(path = "/get_by_id/{id}")
    public ResponseEntity<OffreResponseDTO> getOffre(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(this.offreService.getOffreById(id));
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<String> updateOffre(@RequestBody @Valid  OffreRequestDTO dto, @PathVariable Integer id) {
        this.offreService.updateOffre(id, dto);
        return ResponseEntity.status(202).body("Update successfully");
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteSuccesfully(@PathVariable Integer id) {
        this.offreService.deleteOffre(id);
        return ResponseEntity.status(202).body("Delete successfully");
    }

    @GetMapping(path = "/paginated")
    public ResponseEntity<PageResponseDTO<OffreResponseDTO>> getAllOffresPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateDepart") String sortBy) {
        return ResponseEntity.ok(offreService.getAllOffresPaginated(page, size, sortBy));
    }



    // Alternative avec @RequestParam pour tester facilement
    @GetMapping(path = "/recherche-params")
    public ResponseEntity<List<OffreResponseDTO>> rechercherOffresParams(
            @RequestParam(required = false) String villeDepart,
            @RequestParam(required = false) String villeArrivee,
            @RequestParam(required = false) Double prixMin,
            @RequestParam(required = false) Double prixMax,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDepart,
            @RequestParam(required = false) Integer agenceId) {

        RechercheOffreDTO criteres = new RechercheOffreDTO(
                villeDepart, villeArrivee, prixMin, prixMax, dateDepart, agenceId
        );

        return ResponseEntity.status(200).body(offreService.rechercherOffres(criteres));
    }


    @GetMapping("/search/prix")
    public ResponseEntity<List<OffreResponseDTO>> getOffresByPrixRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        return ResponseEntity.status(200).body(offreService.getOffresByPrixRange(min, max));
    }

    @GetMapping("/search/agence/{agenceId}")
    public ResponseEntity<List<OffreResponseDTO>> getOffresByAgence(@PathVariable Integer agenceId) {
        return ResponseEntity.status(200).body(offreService.getOffresByAgence(agenceId));
    }
    }




