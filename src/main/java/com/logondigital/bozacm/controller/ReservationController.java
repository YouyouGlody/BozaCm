package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.dto.common.ApiResponse;
import com.logondigital.bozacm.dto.reservation.ReservationRequestDTO;
import com.logondigital.bozacm.dto.reservation.ReservationResponseDTO;
import com.logondigital.bozacm.dto.reservation.ReservationUpdateDTO;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.entities.Reservation;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.service.client.ClientService;
import com.logondigital.bozacm.service.reservation.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller REST pour gérer les opérations sur les réservations.

 * Ce controller expose les endpoints de l'API pour :
  - CRUD complet des réservations
  - Historique des réservations (complet, passé, à venir)
  - Filtrage par statut et trajet
  - Actions métier (confirmer, annuler)

 * Utilise des DTOs pour séparer la couche API de la couche métier.
 * Base URL : /api/v1/reservations
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
    private final ClientService clientService;
    private final ReservationMapper reservationMapper;


    // ===========================================================
    // ==========        CRUD DE BASE DES RÉSERVATIONS      ======
    // ===========================================================


    /**
     * Crée une nouvelle réservation.

     * POST /api/v1/reservations
     *
     * @param requestDTO les données de la réservation
     * @return ApiResponse avec ReservationResponseDTO (201 Created)
     */
    @PostMapping(path = "/create")
    public ResponseEntity<ApiResponse> createReservation(@Valid @RequestBody ReservationRequestDTO requestDTO) {
        // 1. Récupérer le client
        Client client =  this.clientService.getClientById(requestDTO.getClientId());

        // 2. Convertir DTO → Entity
        Reservation reservation =  this.reservationMapper.toEntity(requestDTO, client);

        // 3. Créer la réservation
        Reservation reservationCreee =  this.reservationService.createReservation(reservation);

        // 4. Convertir Entity → DTO
        ReservationResponseDTO responseDTO =  this.reservationMapper.toResponseDTO(reservationCreee);

        // 5. Retourner ApiResponse
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Réservation créée avec succès", responseDTO));
    }



    /**
     * Récupère toutes les réservations.

     * GET /api/v1/reservations
     *
     * @return ApiResponse avec List<ReservationResponseDTO> (200 OK)
     */
    @GetMapping(path = "/get_all")
    public ResponseEntity<ApiResponse> getAllReservations() {
        List<Reservation> reservations = this.reservationService.getAllReservations();

        List<ReservationResponseDTO> responseDTOs = reservations.stream()
                .map(this.reservationMapper::toResponseDTO)
                .collect(Collectors.toList());

        String message = String.format("%d réservation(s) récupérée(s)", responseDTOs.size());

        return ResponseEntity.ok(ApiResponse.success(message, responseDTOs));
    }


    /**
     * Récupère une réservation par son ID.

     * GET /api/v1/reservations/{idReservation}
     *
     * @param idReservation l'ID de la réservation
     * @return ApiResponse avec ReservationResponseDTO (200 OK)
     */
    @GetMapping(path = "/get_by_id/{idReservation}")
    public ResponseEntity<ApiResponse> getReservationById(@PathVariable Integer idReservation) {
        Reservation reservation = this.reservationService.getReservationById(idReservation);
        ReservationResponseDTO responseDTO = this.reservationMapper.toResponseDTO(reservation);

        return ResponseEntity.ok(
                ApiResponse.success("Réservation récupérée avec succès", responseDTO));
    }



    /**
     * Met à jour une réservation existante.

     * PUT /api/v1/reservations/{idReservation}
     *
     * @param idReservation l'ID de la réservation
     * @param updateDTO les nouvelles données
     * @return ApiResponse avec ReservationResponseDTO (200 OK)
     */
    @PutMapping(path = "/update/{idReservation}")
    public ResponseEntity<ApiResponse> updateReservation(@PathVariable Integer idReservation, @Valid @RequestBody ReservationUpdateDTO updateDTO) {
        // 1. Récupérer la réservation existante
        Reservation reservation = this.reservationService.getReservationById(idReservation);

        // 2. Mettre à jour avec les données du DTO
        this.reservationMapper.updateEntityFromDTO(reservation, updateDTO);

        // 3. Sauvegarder
        Reservation reservationMiseAJour = this.reservationService.updateReservation(idReservation, reservation);

        // 4. Convertir Entity → DTO
        ReservationResponseDTO responseDTO = this.reservationMapper.toResponseDTO(reservationMiseAJour);

        // 5. Retourner ApiResponse
        return ResponseEntity.ok(
                ApiResponse.success("Réservation mise à jour avec succès", responseDTO));
    }



    /**
     * Supprime une réservation par son ID.

     * DELETE /api/v1/reservations/{idReservation}
     *
     * @param idReservation l'ID de la réservation
     * @return ApiResponse sans données (200 OK)
     */
    @DeleteMapping(path = "/delete/{idReservation}")
    public ResponseEntity<ApiResponse> deleteReservation(@PathVariable Integer idReservation) {
        // Supprime la réservation
        this.reservationService.deleteReservation(idReservation);

        // Retourne 204 No Content (succès sans body)
        return ResponseEntity.ok(ApiResponse.deleted("Réservation supprimée avec succès "));
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
     * { "idReservation": 5, "dateDepart": "2025-10-20", "createdAt": "2025-10-12" },
     * { "idReservation": 3, "dateDepart": "2025-10-15", "createdAt": "2025-10-10" }
     * ]
     */
    @GetMapping("/client/{clientId}/historique")
    public ResponseEntity<ApiResponse> getHistoriqueComplet(@PathVariable Integer clientId) {

        List<Reservation> reservations = this.reservationService.getHistoriqueComplet(clientId);

        List<ReservationResponseDTO> responseDTOs = reservations.stream()
                .map(this.reservationMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                ApiResponse.success("Historique complet récupéré", responseDTOs));
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
     * { "idReservation": 1, "dateDepart": "2025-10-05", "statutReservation": "TERMINEE" }
     * ]
     */
    @GetMapping("/client/{clientId}/historique/passes")
    public ResponseEntity<ApiResponse> getHistoriquePasse(@PathVariable Integer clientId) {
        List<Reservation> reservations = this.reservationService.getHistoriquePasse(clientId);

        List<ReservationResponseDTO> responseDTOs = reservations.stream()
                .map(this.reservationMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                ApiResponse.success("Historique passé récupéré", responseDTOs));
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
     * { "idReservation": 5, "dateDepart": "2025-10-15", "statutReservation": "CONFIRMEE" }
     * ]
     */
    @GetMapping("/client/{clientId}/a-venir")
    public ResponseEntity<ApiResponse> getReservationsAVenir(@PathVariable Integer clientId) {

        List<Reservation> reservations =  this.reservationService.getReservationsAVenir(clientId);

        List<ReservationResponseDTO> responseDTOs = reservations.stream()
                .map(reservationMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("Réservations à venir du client récupérées", responseDTOs));
    }


    // ===========================================================
    // ==========        FILTRAGE ET RECHERCHE              ======
    // ===========================================================


    /**
     * Récupère les réservations d'un client par statut.
     * <p>
     * Endpoint : GET /api/v1/reservations/client/{clientId}/statut/{statut}
     *
     * @param clientId l'ID du client
     * @param statut   le statut recherché (EN_ATTENTE, CONFIRMEE, ANNULEE, TERMINEE)
     * @return liste des réservations avec ce statut (code 200 OK)
     * <p>
     * Exemple de requête :
     * GET /api/v1/reservations/client/1/statut/CONFIRMEE
     * <p>
     * Réponse (200):
     * [
     * { "idReservation": 3, "statutReservation": "CONFIRMEE", ... }
     * ]
     */
    @GetMapping("/client/{clientId}/statut/{statut}")
    public ResponseEntity<ApiResponse> getReservationsParStatut(@PathVariable Integer clientId, @PathVariable StatutReservation statut) {

        List<Reservation> reservations =  this.reservationService.getReservationsParStatut(clientId, statut);

        List<ReservationResponseDTO> responseDTOs = reservations.stream()
                .map(reservationMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                ApiResponse.success("Réservations par statut récupérées", responseDTOs));
    }


    /**
     * Recherche les réservations pour un trajet spécifique.

     * Endpoint : GET /api/v1/reservations/trajet?depart={ville1}&arrivee={ville2}
     *
     * @param depart  la ville de départ
     * @param arrivee la ville d'arrivée
     * @return liste des réservations pour ce trajet (code 200 OK)
     * @RequestParam : Extrait les paramètres de la query string

     * Exemple de requête :
     * GET /api/v1/reservations/trajet?depart=Yaoundé&arrivee=Douala

     * Réponse (200) :
     * [
     * { "idReservation": 1, "villeDeDepart": "Yaoundé", "villeArrivee": "Douala" }
     * ]
     */
    @GetMapping("/trajet")
    public ResponseEntity<ApiResponse> getReservationsParTrajet(@RequestParam String depart, @RequestParam String arrivee) {

        List<Reservation> reservations =  this.reservationService.getReservationsParTrajet(depart, arrivee);

        List<ReservationResponseDTO> responseDTOS = reservations.stream()
                .map(reservationMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                ApiResponse.success("Récupération des réservations par trajet ", responseDTOS));
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
    public ResponseEntity<ApiResponse> countReservationsByClient(@PathVariable Integer clientId) {

        long count =  this.reservationService.countReservationsByClient(clientId);
        String message = String.format("Nombre de réservations : %d", count);

        return ResponseEntity.ok(ApiResponse.success(message, count));
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
    public ResponseEntity<ApiResponse> confirmerReservation(@PathVariable Integer idReservation) {

        Reservation reservation =  this.reservationService.confirmerReservation(idReservation);

        ReservationResponseDTO responseDTO = this.reservationMapper.toResponseDTO(reservation);

        return ResponseEntity.ok(ApiResponse.success("Réservation confirméé avec succès !", responseDTO));
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
    public ResponseEntity<ApiResponse> annulerReservation(@PathVariable Integer idReservation) {

        Reservation reservation = this.reservationService.annulerReservation(idReservation);

        ReservationResponseDTO responseDTO = this.reservationMapper.toResponseDTO(reservation);

        return ResponseEntity.ok(ApiResponse.success("Réservation annuléé avec succès ! ", responseDTO));
    }

}
