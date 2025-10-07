package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.entities.Utilisateur;

import com.logondigital.bozacm.service.utilisateur.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> createUtilisateur(@RequestBody @Valid Utilisateur utilisateur) {
        this.utilisateurService.createUtilisateur(utilisateur);
        return ResponseEntity.status(201).body("Utilisateur créé !");
    }

    @GetMapping(path = "/get_all")
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        return ResponseEntity.status(200).body(this.utilisateurService.getUtilisateurs());
    }

    @GetMapping(path = "/get_by_id/{id}")
    public ResponseEntity<Utilisateur> getUtilisateur(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(this.utilisateurService.getUtilisateurById(id));
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<String> updateUtilisateur(@RequestBody Utilisateur utilisateur, @PathVariable Integer id) {
        this.utilisateurService.updateUtilisateur(id, utilisateur);
        return ResponseEntity.status(202).body("Mise à jour réussie !");
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteUtilisateur(@PathVariable Integer id) {
        this.utilisateurService.deleteUtilisateur(id);
        return ResponseEntity.status(202).body("Suppression réussie !");
    }
}
