package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.dto.common.ApiResponse;
import com.logondigital.bozacm.entities.Billet;
import com.logondigital.bozacm.enums.StatutBillet;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.service.billet.BilletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST pour gérer les opérations sur les billets.
 * <p>
 * Ce controller expose les endpoints de l'API pour :
 *      - CRUD complet des billets
 *      - Recherche par numéro de billet (validation QR code)
 *      - Filtrage par client et statut
 *      - Actions métier (marquer utilisé, expirer billets).
 * <p>
 * Base URL : /api/v1/billets
 *
 * @RestController : Combine @Controller + @ResponseBody
 *                   Toutes les méthodes retournent du JSON automatiquement
 */
@RestController
@RequestMapping("/api/v1/billets")
@RequiredArgsConstructor //pour l'injection de dépendances

public class BilletController {

    /**
     * Service contenant la logique métier des billets.
     * Injection par constructeur.
     */
    private final BilletService billetService;


    // ========================================================================
    // ==========              CRUD DE BASE                          ==========
    // ========================================================================



    /**
     * Crée un nouveau billet.

     * Endpoint : POST /api/v1/billets
     *
     * @param billet les données du billet à créer (JSON dans le body)
     * @return message de confirmation (code 201 Created)

     * Note : Le numéro de billet, la date d'émission et le statut
     *        sont générés automatiquement par @PrePersist

     * Exemple de requête :
     * POST /api/v1/billets
     * Body: {
     *   "client": { "idClient": 1 },
     *   "reservation": { "idReservation": 1 }
     * }

     * Réponse (201) :
     * "Billet créé avec succès"
     */
    @PostMapping(path = "/create")
    public ResponseEntity<ApiResponse> createBillet(@Valid @RequestBody Billet billet) {
        //Créer le billet via le service
        Billet billetCree = this.billetService.createBillet(billet);
        // Retourne le billet créé avec un message de confirmation grâce à l'apiResponse
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Billet créé avec succès", billetCree));
    }



    /**
     * Récupère tous les billets.
     * <p>
     * Endpoint : GET /api/v1/billets
     *
     * @return liste de tous les billets (code 200 OK)
     * <p>
     * Exemple de requête :
     * GET /api/v1/billets
     * <p>
     * Réponse (200) :
     * [
     * { "idBillet": 1, "numeroBillet": "BZC-abc123", "statutBillet": "VALIDE", ... },
     * { "idBillet": 2, "numeroBillet": "BZC-def456", "statutBillet": "UTILISE", ... }
     * ]
     */
    @GetMapping(path = "/get_all")
    public ResponseEntity<ApiResponse> getAllBillets() {
        List<Billet> billets = this.billetService.getAllBillets();
        return ResponseEntity.ok(ApiResponse.success("Récupération de tous les billets des clients ", billets));
    }



    /**
     * Récupère un billet par son ID.

     * Endpoint : GET /api/v1/billets/{id}

     * @param idBillet l'ID du billet à récupérer
     * @return le billet trouvé (code 200 OK)
     * @throws RessourceNotFoundException si le billet n'existe pas

     * Exemple de requête :
     * GET /api/v1/billets/1

     * Réponse (200) :
     * {
     *   "idBillet": 1,
     *   "numeroBillet": "BZC-abc123",
     *   "statutBillet": "VALIDE",
     *   "dateEmission": "2025-10-12T14:30:00",
     *   "dateExpiration": "2025-10-13T14:30:00",
     *   ...
     * }
     */
    @GetMapping(path = "/get_by_id/{idBillet}")
    public ResponseEntity<ApiResponse> getBilletById(@PathVariable Integer idBillet) {
        Billet billet = this.billetService.getBilletById(idBillet);
        return ResponseEntity.ok(ApiResponse.success("Récupération du billet avec l'ID " + idBillet, billet));
    }



    /**
     * Supprime un billet par son ID.

     * Endpoint : DELETE /api/v1/billets/{id}
     *
     * @param idBillet l'ID du billet à supprimer
     * @return pas de contenu (code 204 No Content)
     * @throws RessourceNotFoundException si le billet n'existe pas

     * Convention REST : DELETE retourne 204 (pas de body)

     * Exemple de requête :
     * DELETE /api/v1/billets/1

     * Réponse (204) : Pas de contenu (succès)
     */
    @DeleteMapping(path = "/delete/{idBillet}")
    public ResponseEntity<ApiResponse> deleteBillet(@PathVariable Integer idBillet) {
        // Supprime le billet
        this.billetService.deleteBilletById(idBillet);
        // Retourne 204 No Content
        return ResponseEntity.ok(ApiResponse.deleted("Suppression du billet avec succès de l'ID " + idBillet));
    }



    /**
     * Compte le nombre total de billets.

     * Endpoint : GET /api/v1/billets/count
     *
     * @return le nombre total de billets (code 200 OK)

     * Exemple de requête :
     * GET /api/v1/billets/count

     * Réponse (200) : 42
     */
    @GetMapping(path = "/count")
    public ResponseEntity<ApiResponse> countBillets() {
        long count = this.billetService.countBillets();
        return ResponseEntity.ok(ApiResponse.success("Le nombre total de billets est de : ", count));
    }


    // ========================================================================
    // ==========              MÉTHODES MÉTIER                       ==========
    // ========================================================================



    /**
     * Recherche un billet par son numéro unique.
     * Utilisé pour la validation du QR code à l'embarquement.

     * Endpoint : GET /api/v1/billets/numero/{numeroBillet}
     *
     * @param numeroBillet le numéro unique du billet (format : BZC-xxx)
     * @return le billet trouvé (code 200 OK)
     * @throws RessourceNotFoundException si le billet n'existe pas

     * Cas d'usage : Scanner le QR code du billet à l'embarquement

     * Exemple de requête :
     * GET /api/v1/billets/numero/BZC-a1b2c3d4-e5f6-7890-abcd-ef1234567890

     * Réponse (200) :
     * {
     *   "idBillet": 1,
     *   "numeroBillet": "BZC-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
     *   "statutBillet": "VALIDE",
     *   "nomClientSurBillet": "Dupont",
     *   "prenomClientSurBillet": "Jean",
     *   ...
     * }
     */
    @GetMapping("/numero/{numeroBillet}")
    public ResponseEntity<ApiResponse> getBilletByNumero(@PathVariable String numeroBillet) {
        Billet billet = this.billetService.findByNumeroBillet(numeroBillet);
        return ResponseEntity.ok(ApiResponse.success("Récupération du billet du client ", billet));
    }

    /**
     * Récupère tous les billets d'un client.
     * Triés par date d'émission (plus récent en premier).
     * <p>
     * Endpoint : GET /api/v1/billets/client/{clientId}
     *
     * @param clientId l'ID du client
     * @return liste des billets du client (code 200 OK)
     * <p>
     * Exemple de requête :
     * GET /api/v1/billets/client/1
     * <p>
     * Réponse (200) :
     * [
     * { "idBillet": 5, "numeroBillet": "BZC-xyz", "dateEmission": "2025-10-12", ... },
     * { "idBillet": 3, "numeroBillet": "BZC-abc", "dateEmission": "2025-10-08", ... }
     * ]
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponse> getBilletsClient(@PathVariable Integer clientId) {
        List<Billet> billets = this.billetService.getBilletsClient(clientId);
        return ResponseEntity.ok(ApiResponse.success("Récupération des billets du client par ordre croissant !", billets));
    }

    /**
     * Récupère les billets d'un client par statut.
     * <p>
     * Endpoint : GET /api/v1/billets/client/{clientId}/statut/{statut}
     *
     * @param clientId l'ID du client
     * @param statut   le statut recherché (VALIDE, UTILISE, EXPIRE, ANNULE)
     * @return liste des billets avec ce statut (code 200 OK)
     * <p>
     * Exemple de requête :
     * GET /api/v1/billets/client/1/statut/VALIDE
     * <p>
     * Réponse (200) :
     * [
     * { "idBillet": 3, "statutBillet": "VALIDE", "dateExpiration": "2025-10-20", ... },
     * { "idBillet": 5, "statutBillet": "VALIDE", "dateExpiration": "2025-10-25", ... }
     * ]
     */
    @GetMapping("/client/{clientId}/statut/{statut}")
    public ResponseEntity<ApiResponse> getBilletsParStatut(
            @PathVariable Integer clientId,
            @PathVariable StatutBillet statut) {

        List<Billet> billets = this.billetService.getBilletsParStatut(clientId, statut);
        return ResponseEntity.ok(ApiResponse.success("Récupération des billets du client par statut", billets));
    }

    /**
     * Récupère le billet associé à une réservation.

     * Endpoint : GET /api/v1/billets/reservation/{reservationId}

     * @param reservationId l'ID de la réservation
     * @return le billet associé (code 200 OK)
     * @throws RessourceNotFoundException si aucun billet n'existe pour cette réservation

     * Relation One-to-One : Une réservation = Un billet unique

     * Exemple de requête :
     * GET /api/v1/billets/reservation/1

     * Réponse (200) :
     * {
     *   "idBillet": 1,
     *   "numeroBillet": "BZC-abc123",
     *   "reservation": { "idReservation": 1, ... },
     *   ...
     * }
     */
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<ApiResponse> getBilletByReservation(
            @PathVariable Integer reservationId) {

        Billet billet = this.billetService.getBilletByReservation(reservationId);
        return ResponseEntity.ok(ApiResponse.success("Récupération des billets du client par réservation", billet));
    }

    /**
     * Compte le nombre de billets d'un client.

     * Endpoint : GET /api/v1/billets/client/{clientId}/count
     *
     * @param clientId l'ID du client
     * @return le nombre de billets du client (code 200 OK)

     * Exemple de requête :
     * GET /api/v1/billets/client/1/count

     * Réponse (200) : 8
     */
    @GetMapping("/client/{clientId}/count")
    public ResponseEntity<ApiResponse> countBilletsByClient(@PathVariable Integer clientId) {
        long count = this.billetService.countBilletsByClient(clientId);
        return ResponseEntity.ok(ApiResponse.success("Le nombre de billet est de : ", count));
    }

    // ========================================================================
    // ==========              ACTIONS MÉTIER                        ==========
    // ========================================================================

    /**
     * Marque un billet comme utilisé (scan à l'embarquement).
     * Change le statut du billet de VALIDE à UTILISE.

     * Endpoint : PATCH /api/v1/billets/numero/{numeroBillet}/utiliser
     *
     * @param numeroBillet le numéro du billet à marquer comme utilisé
     * @return le billet mis à jour (code 200 OK)
     * @throws RessourceNotFoundException si le billet n'existe pas

     * Cas d'usage :
     *      - Le client monte dans le bus/train/avion
     *      - L'agent scanne le QR code du billet
     *      - Le système appelle cet endpoint pour marquer le billet comme utilisé

     * @PatchMapping : Modification partielle (juste le statut)

     * Exemple de requête :
     * PATCH /api/v1/billets/numero/BZC-abc123/utiliser

     * Réponse (200) :
     * {
     *   "idBillet": 1,
     *   "numeroBillet": "BZC-abc123",
     *   "statutBillet": "UTILISE",
     *   "updatedAt": "2025-10-12T16:00:00",
     *   ...
     * }
     */
    @PatchMapping("/numero/{numeroBillet}/utiliser")
    public ResponseEntity<ApiResponse> marquerBilletUtilise(@PathVariable String numeroBillet) {
        Billet billet = this.billetService.marquerBilletUtilise(numeroBillet);
        return ResponseEntity.ok(ApiResponse.success("Billet utilisé ! ", billet));
    }

    /**
     * Expire automatiquement tous les billets périmés.
     * Parcourt tous les billets dont la dateExpiration est passée
     * et change leur statut de VALIDE à EXPIRE.

     * Endpoint : POST /api/v1/billets/expirer-perimes
     *
     * @return message de confirmation (code 200 OK)

     * Cas d'usage :
     *      - Job planifié (Scheduled Task) qui s'exécute automatiquement chaque jour
     *      - Ou appel manuel pour nettoyer les billets périmés

     * Logique :
     * 1. Récupère tous les billets avec dateExpiration < maintenant
     * 2. Pour chaque billet VALIDE, change le statut à EXPIRE
     * 3. Sauvegarde les modifications

     * Exemple de requête :
     * POST /api/v1/billets/expirer-perimes

     * Réponse (200) :
     * "Billets périmés expirés avec succès"

     * Note : Cette méthode peut être appelée par un @Scheduled dans le service
     *        pour une exécution automatique quotidienne
     */
    @PostMapping("/expirer-perimes")
    public ResponseEntity<ApiResponse> expirerBilletsPerimes() {
        // Expire tous les billets périmés
        this.billetService.expirerBilletsPerimes();

        // Retourne un message de confirmation
        return ResponseEntity.ok(ApiResponse.success("Billet utilisé"));
    }



}
