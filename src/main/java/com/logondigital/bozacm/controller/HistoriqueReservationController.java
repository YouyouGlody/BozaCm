package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.entities.HistoriqueReservation;
import com.logondigital.bozacm.service.historiqueReservation.HistoriqueReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrôleur REST pour gérer les opérations sur les historiques de réservation.
 * Ce contrôleur expose des endpoints pour créer, lister et supprimer des entrées
 * d'historique de réservation.
 */
@RestController
@RequestMapping("/api/v1/historiques")
@RequiredArgsConstructor

public class HistoriqueReservationController {

    private final HistoriqueReservationService historiqueService;

    /**
     * Crée une nouvelle entrée dans l'historique des réservations.
     *
     * @param historiqueReservation L'objet historique à créer
     * @return Un message de confirmation avec le statut HTTP 201 (Created)
     */

    @PostMapping("/create")
    public ResponseEntity<String> createHistorique(@Valid @RequestBody HistoriqueReservation historiqueReservation) {
        // Définir la date de création si elle n'est pas déjà définie
        if (historiqueReservation.getCreationDate() == null) {
            historiqueReservation.setCreationDate(LocalDateTime.now());
        }

        this.historiqueService.addHistorique(historiqueReservation);
        return ResponseEntity.status(201).body("L'historique de réservation a été créé avec succès !");
    }

    /**
     * Récupère toutes les entrées d'historique de réservation.
     *
     * @return La liste de tous les historiques avec le statut HTTP 200 (OK)
     */
    @GetMapping("/get_all")
    public ResponseEntity<List<HistoriqueReservation>> getAllHistoriques() {
        List<HistoriqueReservation> historiques = this.historiqueService.getAllHistoriques();
        return ResponseEntity.ok(historiques);
    }

    /**
     * Récupère l'historique des réservations pour un client spécifique.
     *
     * @param idClient L'identifiant du client
     * @return La liste des historiques du client avec le statut HTTP 200 (OK)
     */
    @GetMapping("/client/{idClient}")
    public ResponseEntity<List<HistoriqueReservation>> getHistoriquesByClient(@PathVariable Integer idClient) {
        List<HistoriqueReservation> historiques = this.historiqueService.findByClient_IdClient(idClient);
        return ResponseEntity.ok(historiques);
    }


    /**
     * Supprime une entrée spécifique de l'historique des réservations.
     *
     * @param idHistorique L'identifiant de l'historique à supprimer
     * @return Un message de confirmation avec le statut HTTP 202 (Accepted)
     */
    @DeleteMapping("/delete/{idHistorique}")
    public ResponseEntity<String> deleteHistorique(@PathVariable Integer idHistorique) {
        this.historiqueService.deleteHistorique(idHistorique);
        return ResponseEntity.status(202)
                .body("L'historique de réservation avec l'ID " + idHistorique + " a été supprimé avec succès !");
    }




}
