package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.entities.Billet;
import com.logondigital.bozacm.service.billet.BilletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/billets")
@RequiredArgsConstructor //pour l'injection de dépendances

public class BilletController {

    private final BilletService billetService;


    // ✅ Créer un billet
    @PostMapping(path = "/create")
    public ResponseEntity<String> createBillet(@RequestBody Billet billet) {
        //Créer le billet
        this.billetService.createBillet(billet);
        //Retourne le message
        return ResponseEntity.status(201).body("Le Billet a été créé avec succès !");
    }

    // ✅ Récupérer tous les billets
    @GetMapping(path = "/get_all")
    public ResponseEntity<List<Billet>> getAllBillets() {
        return ResponseEntity.status(200).body(this.billetService.getAllBillets());
    }


    // ✅ Récupérer un billet par ID
    @GetMapping(path = "/get_by_id/{idBillet}")
    public ResponseEntity<Billet> getBilletById(@PathVariable Integer idBillet) {
        return ResponseEntity.status(200).body(this.billetService.getBilletById(idBillet));
    }


    // ✅ Supprimer un billet
    @DeleteMapping(path = "/delete/{idBillet}")
    public ResponseEntity<String> deleteBillet(@PathVariable Integer idBillet) {
        // Récupérer le billet avant suppression pour le message
        Billet billet = billetService.getBilletById(idBillet);
        // Supprimer le billet
        this.billetService.deleteBilletById(idBillet);
        // Retourner le message
        return ResponseEntity.status(202)
                .body("Le Billet avec l'ID " + idBillet + " a été supprimé avec succès !");
    }


    // ✅ Supprimer tous les billets
    @DeleteMapping(path = "/delete_all")
    public ResponseEntity<String> deleteAllBillets() {
        this.billetService.deleteAllBillets();
        return ResponseEntity.status(202)
                .body("Tous les billets ont été supprimés avec succès !");
    }


    // ✅ Obtenir le nombre total de billets
    @GetMapping(path = "/count")
    public ResponseEntity<Long> countBillets() {
        return ResponseEntity.status(200).body(this.billetService.countBillets());
    }



}
