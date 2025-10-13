package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.entities.Reservation;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.service.reservation.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Controller REST pour gérer les opérations sur les réservations.

 * Ce controller expose les endpoints de l'API pour :
  - CRUD complet des réservations
  - Historique des réservations (complet, passé, à venir)
  - Filtrage par statut et trajet
  - Actions métier (confirmer, annuler)

 * Base URL : /api/v1/reservations

 * @RestController : Combine @Controller + @ResponseBody
 *                   Toutes les méthodes retournent du JSON automatiquement

 * @RequestMapping : Définit l'URL de base pour ce controller
 */

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor //pour l'injection de dépendances

public class ReservationController {

    /**
     * Service contenant la logique métier des réservations.
     * Injection par constructeur (pas besoin de @Autowired avec un seul constructeur).
     */
    private final ReservationService reservationService;


    // ===========================================================
    // ==========        CRUD DE BASE DES RÉSERVATIONS      ======
    // ===========================================================


    /**
     * Crée une nouvelle réservation.

     * Endpoint : POST /api/v1/reservations
     *
     * @param reservation les données de la réservation à créer (JSON dans le body)
     * @return la réservation créée avec son ID généré (code 201 Created)

     * Exemple de requête :
     * POST /api/v1/reservations
     * Body: {
     * "villeDeDepart": "Yaoundé",
     * "villeArrivee": "Douala",
     * "dateDepart": "2025-10-20T10:00:00",
     * "client": { "idClient": 1 }
     * }

     * Réponse (201):
     * {
     * "idReservation": 1,
     * "villeDeDepart": "Yaoundé",
     * "villeArrivee": "Douala",
     * "dateDepart": "2025-10-20T10:00:00",
     * "statutReservation": "EN_ATTENTE",
     * "createdAt": "2025-10-13T14:30:00"
     * }
     */
    @PostMapping(path = "/create")
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody Reservation reservation) {
        // Crée la réservation via le service
        Reservation reservationCreee = reservationService.createReservation(reservation);
        // Retourne la réservation créée avec code 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationCreee);
    }


    /**
     * Récupère toutes les réservations.

     * Endpoint : GET /api/v1/reservations
     *
     * @return liste de toutes les réservations (code 200 OK)

     * Exemple de requête :
     * GET /api/v1/reservations

     * Réponse (200) :
     * [
     *   { "idReservation": 1, "villeDeDepart": "Yaoundé", ... },
     *   { "idReservation": 2, "villeDeDepart": "Douala", ... }
     * ]
     */
    @GetMapping(path = "/get_all")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }


    /**
     * Récupère une réservation par son ID.

     * Endpoint : GET /api/v1/reservations/{id}
     *
     * @param idReservation l'ID de la réservation à récupérer
     * @return la réservation trouvée (code 200 OK)
     * @throws RessourceNotFoundException si la réservation n'existe pas

     * Exemple de requête :
     * GET /api/v1/reservations/1

     * Réponse (200) :
     * {
     *   "idReservation": 1,
     *   "villeDeDepart": "Yaoundé",
     *   "villeArrivee": "Douala",
     *   "statutReservation": "CONFIRMEE",
     *   ...
     * }
     */
    @GetMapping(path = "/get_by_id/{idReservation}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Integer idReservation) {
        Reservation reservation = reservationService.getReservationById(idReservation);
        return ResponseEntity.ok(reservation);
    }



    /**
     * Met à jour une réservation existante.
     * <p>
     * Endpoint: PUT /api/v1/reservations/{id}
     *
     * @param idReservation l'ID de la réservation à mettre à jour
     * @param reservation   les nouvelles données de la réservation
     * @return la réservation mise à jour (code 200 OK)
     * @throws RessourceNotFoundException si la réservation n'existe pas

     * Exemple de requête :
     *  PUT /api/v1/reservations/1
     *             Body:     {
     *                        "villeDeDepart": "Yaoundé",
     *                        "villeArrivee": "Bafoussam",
     *                        "dateDepart": "2025-10-25T15:00:00",
     *                        "statutReservation": "CONFIRMEE"
     *                       }

     *    Réponse (200) :
     *                       {
     *                         "idReservation": 1,
     *                         "villeDeDepart": "Yaoundé",
     *                         "villeArrivee": "Bafoussam",
     *                         "updatedAt": "2025-10-13T15:00:00"
     *                        }
     */
    @PutMapping(path = "/update/{idReservation}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Integer idReservation, @Valid @RequestBody Reservation reservation) {
        // Met à jour la réservation
        Reservation reservationMiseAJour = reservationService.updateReservation(
                idReservation, reservation);
        //Retourne le message
        return ResponseEntity.ok(reservationMiseAJour);
    }



    /**
     * Supprime une réservation par son ID.

     * Endpoint : DELETE /api/v1/reservations/{id}
     *
     * @param idReservation l'ID de la réservation à supprimer
     * @return pas de contenu (code 204 No Content)
     * @throws RessourceNotFoundException si la réservation n'existe pas

     * Convention REST : DELETE retourne 204 (pas de body)

     * Exemple de requête :
     * DELETE /api/v1/reservations/1
     *
     * Réponse (204) : Pas de contenu (succès)
     */
    @DeleteMapping(path = "/delete/{idReservation}")
    public ResponseEntity<String> deleteReservation(@PathVariable Integer idReservation) {
        // Supprime la réservation
        reservationService.deleteReservation(idReservation);

        // Retourne 204 No Content (succès sans body)
        return ResponseEntity.noContent().build();
    }




    // ===========================================================
    // ==========        HISTORIQUE CLIENT                  ======
    // ===========================================================


    /**
     * Récupère l'historique COMPLET des réservations d'un client.
     * Trié par date de création (plus récent en premier).

     * Endpoint : GET /api/v1/reservations/client/{clientId}/historique
     *
     * @param clientId l'ID du client
     * @return liste des réservations du client (code 200 OK)

     * Exemple de requête :
     * GET /api/v1/reservations/client/1/historique

     * Réponse (200) :
     * [
     *   { "idReservation": 5, "dateDepart": "2025-10-20", "createdAt": "2025-10-12" },
     *   { "idReservation": 3, "dateDepart": "2025-10-15", "createdAt": "2025-10-10" }
     * ]
     */
    @GetMapping("/client/{clientId}/historique")
    public ResponseEntity<List<Reservation>> getHistoriqueComplet(@PathVariable Integer clientId) {
        List<Reservation> historique = reservationService.getHistoriqueComplet(clientId);
        return ResponseEntity.ok(historique);
    }


    /**
     * Récupère les réservations PASSÉES d'un client (voyages déjà effectués).

     * Endpoint : GET /api/v1/reservations/client/{clientId}/historique/passes
     *
     * @param clientId l'ID du client
     * @return liste des réservations passées (code 200 OK)

     * Filtre : dateDepart < maintenant

     * Exemple de requête :
     * GET /api/v1/reservations/client/1/historique/passes

     * Réponse (200) :
     * [
     *   { "idReservation": 1, "dateDepart": "2025-10-05", "statutReservation": "TERMINEE" }
     * ]
     */
    @GetMapping("/client/{clientId}/historique/passes")
    public ResponseEntity<List<Reservation>> getHistoriquePasse(@PathVariable Integer clientId) {
        List<Reservation> historique = reservationService.getHistoriquePasse(clientId);
        return ResponseEntity.ok(historique);
    }

    /**
     * Récupère les réservations À VENIR d'un client (voyages futurs).
     * Triées par date de départ (plus proche en premier).

     * Endpoint : GET /api/v1/reservations/client/{clientId}/a-venir
     *
     * @param clientId l'ID du client
     * @return liste des réservations à venir (code 200 OK)

     * Filtre : dateDepart >= maintenant

     * Exemple de requête :
     * GET /api/v1/reservations/client/1/a-venir

     * Réponse (200) :
     * [
     *   { "idReservation": 5, "dateDepart": "2025-10-15", "statutReservation": "CONFIRMEE" }
     * ]
     */
    @GetMapping("/client/{clientId}/a-venir")
    public ResponseEntity<List<Reservation>> getReservationsAVenir(@PathVariable Integer clientId) {
        List<Reservation> reservations = reservationService.getReservationsAVenir(clientId);
        return ResponseEntity.ok(reservations);
    }


    // ===========================================================
    // ==========        FILTRAGE ET RECHERCHE              ======
    // ===========================================================


    /**
     * Récupère les réservations d'un client par statut.

     * Endpoint : GET /api/v1/reservations/client/{clientId}/statut/{statut}
     *
     * @param clientId l'ID du client
     * @param statut le statut recherché (EN_ATTENTE, CONFIRMEE, ANNULEE, TERMINEE)
     * @return liste des réservations avec ce statut (code 200 OK)

     * Exemple de requête :
     * GET /api/v1/reservations/client/1/statut/CONFIRMEE

     * Réponse (200) :
     * [
     *   { "idReservation": 3, "statutReservation": "CONFIRMEE", ... }
     * ]
     */
    @GetMapping("/client/{clientId}/statut/{statut}")
    public ResponseEntity<List<Reservation>> getReservationsParStatut(@PathVariable Integer clientId, @PathVariable StatutReservation statut) {
        List<Reservation> reservations = reservationService.getReservationsParStatut(clientId, statut);
        return ResponseEntity.ok(reservations);
    }


    /**
     * Recherche les réservations pour un trajet spécifique.

     * Endpoint : GET /api/v1/reservations/trajet?depart={ville1}&arrivee={ville2}
     *
     * @param depart la ville de départ
     * @param arrivee la ville d'arrivée
     * @return liste des réservations pour ce trajet (code 200 OK)
     *
     * @RequestParam : Extrait les paramètres de la query string

     * Exemple de requête :
     * GET /api/v1/reservations/trajet?depart=Yaoundé&arrivee=Douala

     * Réponse (200) :
     * [
     *   { "idReservation": 1, "villeDeDepart": "Yaoundé", "villeArrivee": "Douala" }
     * ]
     */
    @GetMapping("/trajet")
    public ResponseEntity<List<Reservation>> getReservationsParTrajet(@RequestParam String depart, @RequestParam String arrivee) {
        List<Reservation> reservations = reservationService.getReservationsParTrajet(depart, arrivee);
        return ResponseEntity.ok(reservations);
    }


    /**
     * Compte le nombre total de réservations d'un client.

     * Endpoint : GET /api/v1/reservations/client/{clientId}/count
     *
     * @param clientId l'ID du client
     * @return le nombre de réservations (code 200 OK)

     * Exemple de requête :
     * GET /api/v1/reservations/client/1/count
     *
     * Réponse (200) : 12
     */
    @GetMapping("/client/{clientId}/count")
    public ResponseEntity<Long> countReservationsByClient(@PathVariable Integer clientId) {
        long count = reservationService.countReservationsByClient(clientId);
        return ResponseEntity.ok(count);
    }



    // ===========================================================
    // ==========        ACTIONS MÉTIER                     ======
    // ===========================================================


    /**
     * Confirme une réservation (change le statut à CONFIRMEE).

     * Endpoint : PATCH /api/v1/reservations/{id}/confirmer
     *
     * @param idReservation l'ID de la réservation à confirmer
     * @return la réservation confirmée (code 200 OK)
     * @throws RessourceNotFoundException si la réservation n'existe pas
     *
     * @PatchMapping : HTTP PATCH pour une modification partielle

     * Exemple de requête :
     * PATCH /api/v1/reservations/1/confirmer

     * Réponse (200) :
     * {
     *   "idReservation": 1,
     *   "statutReservation": "CONFIRMEE",
     *   "updatedAt": "2025-10-13T16:00:00"
     * }
     */
    @PatchMapping("/{idReservation}/confirmer")
    public ResponseEntity<Reservation> confirmerReservation(@PathVariable Integer idReservation) {
        Reservation reservation = reservationService.confirmerReservation(idReservation);
        return ResponseEntity.ok(reservation);
    }


    /**
     * Annule une réservation (change le statut à ANNULEE).

     * Endpoint : PATCH /api/v1/reservations/{id}/annuler
     *
     * @param idReservation l'ID de la réservation à annuler
     * @return la réservation annulée (code 200 OK)
     * @throws RessourceNotFoundException si la réservation n'existe pas

     * Exemple de requête :
     * PATCH /api/v1/reservations/1/annuler

     * Réponse (200) :
     * {
     *   "idReservation": 1,
     *   "statutReservation": "ANNULEE",
     *   "updatedAt": "2025-10-13T16:05:00"
     * }
     */
    @PatchMapping("/{idReservation}/annuler")
    public ResponseEntity<Reservation> annulerReservation(@PathVariable Integer idReservation) {
        Reservation reservation = reservationService.annulerReservation(idReservation);
        return ResponseEntity.ok(reservation);
    }

}
