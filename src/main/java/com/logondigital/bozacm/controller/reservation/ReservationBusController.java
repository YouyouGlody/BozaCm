package com.logondigital.bozacm.controller.reservation;

import com.logondigital.bozacm.dto.common.ApiResponse;
import com.logondigital.bozacm.dto.mapper.reservation.ReservationBusMapper;
import com.logondigital.bozacm.dto.reservation.bus.ReservationBusRequestDTO;
import com.logondigital.bozacm.dto.reservation.bus.ReservationBusResponseDTO;
import com.logondigital.bozacm.dto.reservation.bus.ReservationBusUpdateDTO;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.entities.reservation.ReservationBus;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.TypeBus;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.service.client.ClientService;
import com.logondigital.bozacm.service.reservation.ReservationBusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST pour gérer les réservations de BUS.

 * Endpoints disponibles :
 * - CRUD complet des réservations bus
 * - Historique client (complet, passé, à venir)
 * - Filtrage par statut et trajet
 * - Actions métier (confirmer, annuler)
 * - Recherches spécifiques bus (compagnie, type, climatisation)

 * Base URL : /api/v1/reservations/bus

 * Utilise :
 * - ReservationBusService pour la logique métier
 * - ReservationBusMapper pour les conversions Entity ↔ DTO
 * - ClientService pour récupérer les clients
 * - ApiResponse pour des réponses uniformes
 */
@RestController
@RequestMapping("/api/v1/reservations/bus")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Autoriser toutes les origines (à restreindre en production)
public class ReservationBusController {

    private final ReservationBusService reservationBusService;
    private final ClientService clientService;
    private final ReservationBusMapper reservationBusMapper;

    // ===========================================================
    // ==========        CRUD DE BASE                       ======
    // ===========================================================

    /**
     * Crée une nouvelle réservation de bus.

     * POST /api/v1/reservations/bus
     *
     * @param requestDTO les données de la réservation (validées)
     * @return ApiResponse avec ReservationBusResponseDTO (201 Created)

     * Exemple de requête :
     * {
     *   "villeDeDepart": "Douala",
     *   "villeArrivee": "Yaoundé",
     *   "dateDepart": "2025-11-15T08:00:00",
     *   "prixReservation": 5000.0,
     *   "clientId": 5,
     *   "compagnieBus": "Touristique Express",
     *   "typeBus": "VIP",
     *   "climatisation": true
     * }
     */
    @PostMapping(path = "/create")
    public ResponseEntity<ApiResponse> createReservation(
            @Valid @RequestBody ReservationBusRequestDTO requestDTO) {

        // 1. Récupérer le client
        Client client = this.clientService.getClientById(requestDTO.getClientId());

        // 2. Convertir DTO → Entity
        ReservationBus reservation = this.reservationBusMapper.toEntity(requestDTO, client);

        // 3. Créer la réservation
        ReservationBus reservationCreee = this.reservationBusService.createReservation(reservation);

        // 4. Convertir Entity → DTO
        ReservationBusResponseDTO responseDTO = this.reservationBusMapper.toResponseDTO(reservationCreee);

        // 5. Retourner ApiResponse
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Réservation de bus créée avec succès", responseDTO));
    }

    /**
     * Récupère toutes les réservations de bus.

     * GET /api/v1/reservations/bus
     *
     * @return ApiResponse avec List<ReservationBusResponseDTO> (200 OK)
     */
    @GetMapping(path ="/get_all" )
    public ResponseEntity<ApiResponse> getAllReservations() {
        List<ReservationBus> reservations = this.reservationBusService.getAllReservations();
        List<ReservationBusResponseDTO> responseDTOs = this.reservationBusMapper.toResponseDTOList(reservations);

        String message = String.format("%d réservation(s) de bus récupérée(s)", responseDTOs.size());
        return ResponseEntity.ok(ApiResponse.success(message, responseDTOs));
    }

    /**
     * Récupère une réservation de bus par son ID.

     * GET /api/v1/reservations/bus/{id}
     *
     * @param id l'ID de la réservation
     * @return ApiResponse avec ReservationBusResponseDTO (200 OK)
     * @throws RessourceNotFoundException si la réservation n'existe pas
     */
    @GetMapping("/get_reservation_by_id/{id}")
    public ResponseEntity<ApiResponse> getReservationById(@PathVariable Integer id) {
        ReservationBus reservation = this.reservationBusService.getReservationById(id);
        ReservationBusResponseDTO responseDTO = this.reservationBusMapper.toResponseDTO(reservation);

        return ResponseEntity.ok(
                ApiResponse.success("Réservation de bus récupérée avec succès", responseDTO));
    }

    /**
     * Met à jour une réservation de bus existante.

     * PUT /api/v1/reservations/bus/{id}
     *
     * @param id l'ID de la réservation
     * @param updateDTO les nouvelles données (validées)
     * @return ApiResponse avec ReservationBusResponseDTO (200 OK)
     *
     * Note : Mise à jour partielle - seuls les champs fournis sont modifiés
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateReservation(@PathVariable Integer id, @Valid @RequestBody ReservationBusUpdateDTO updateDTO) {

        // 1. Récupérer la réservation existante
        ReservationBus reservation = this.reservationBusService.getReservationById(id);

        // 2. Appliquer les modifications
        this.reservationBusMapper.updateEntityFromDTO(reservation, updateDTO);

        // 3. Sauvegarder
        ReservationBus reservationMiseAJour = this.reservationBusService.updateReservation(id, reservation);

        // 4. Convertir Entity → DTO
        ReservationBusResponseDTO responseDTO = this.reservationBusMapper.toResponseDTO(reservationMiseAJour);

        // 5. Retourner ApiResponse
        return ResponseEntity.ok(
                ApiResponse.success("Réservation de bus mise à jour avec succès", responseDTO));
    }

    /**
     * Supprime une réservation de bus par son ID.

     * DELETE /api/v1/reservations/bus/{id}
     *
     * @param id l'ID de la réservation
     * @return ApiResponse sans données (200 OK)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteReservation(@PathVariable Integer id) {
        reservationBusService.deleteReservation(id);
        return ResponseEntity.ok(ApiResponse.deleted("Réservation de bus supprimée avec succès"));
    }

    // ===========================================================
    // ==========        HISTORIQUE CLIENT                  ======
    // ===========================================================

    /**
     * Récupère l'historique COMPLET des réservations de bus d'un client.
     * Trié par date de création décroissante (plus récent en premier).

     * GET /api/v1/reservations/bus/client/{clientId}/historique
     *
     * @param clientId l'ID du client
     * @return liste des réservations (200 OK)
     */
    @GetMapping("/client/{clientId}/historique")
    public ResponseEntity<ApiResponse> getHistoriqueComplet(@PathVariable Integer clientId) {
        List<ReservationBus> reservations = reservationBusService.getHistoriqueComplet(clientId);
        List<ReservationBusResponseDTO> responseDTOs = reservationBusMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Historique complet des réservations bus récupéré", responseDTOs));
    }

    /**
     * Récupère les réservations de bus PASSÉES d'un client.
     * Filtre : dateDepart < maintenant

     * GET /api/v1/reservations/bus/client/{clientId}/historique/passes
     *
     * @param clientId l'ID du client
     * @return liste des réservations passées (200 OK)
     */
    @GetMapping("/client/{clientId}/historique/passes")
    public ResponseEntity<ApiResponse> getHistoriquePasse(@PathVariable Integer clientId) {
        List<ReservationBus> reservations = reservationBusService.getHistoriquePasse(clientId);
        List<ReservationBusResponseDTO> responseDTOs = reservationBusMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Historique passé des réservations bus récupéré", responseDTOs));
    }

    /**
     * Récupère les réservations de bus À VENIR d'un client.
     * Filtre : dateDepart >= maintenant
     * Trié par date de départ croissante (plus proche en premier).

     * GET /api/v1/reservations/bus/client/{clientId}/a-venir
     *
     * @param clientId l'ID du client
     * @return liste des réservations à venir (200 OK)
     */
    @GetMapping("/client/{clientId}/a-venir")
    public ResponseEntity<ApiResponse> getReservationsAVenir(@PathVariable Integer clientId) {
        List<ReservationBus> reservations = reservationBusService.getReservationsAVenir(clientId);
        List<ReservationBusResponseDTO> responseDTOs = reservationBusMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Réservations bus à venir récupérées", responseDTOs));
    }

    // ===========================================================
    // ==========        FILTRAGE ET RECHERCHE              ======
    // ===========================================================

    /**
     * Récupère les réservations de bus d'un client par statut.

     * GET /api/v1/reservations/bus/client/{clientId}/statut/{statut}
     *
     * @param clientId l'ID du client
     * @param statut le statut recherché (EN_ATTENTE, CONFIRMEE, ANNULEE, COMPLETEE)
     * @return liste des réservations avec ce statut (200 OK)
     */
    @GetMapping("/client/{clientId}/statut/{statut}")
    public ResponseEntity<ApiResponse> getReservationsParStatut(@PathVariable Integer clientId, @PathVariable StatutReservation statut) {

        List<ReservationBus> reservations = reservationBusService.getReservationsParStatut(clientId, statut);
        List<ReservationBusResponseDTO> responseDTOs = reservationBusMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Réservations bus par statut récupérées", responseDTOs));
    }

    /**
     * Recherche les réservations de bus pour un trajet spécifique.

     * GET /api/v1/reservations/bus/trajet?depart={ville1}&arrivee={ville2}
     *
     * @param depart la ville de départ
     * @param arrivee la ville d'arrivée
     * @return liste des réservations pour ce trajet (200 OK)
     *
     * Exemple : GET /api/v1/reservations/bus/trajet?depart=Douala&arrivee=Yaoundé
     */
    @GetMapping("/trajet")
    public ResponseEntity<ApiResponse> getReservationsParTrajet(@RequestParam String depart, @RequestParam String arrivee) {

        List<ReservationBus> reservations = reservationBusService.getReservationsParTrajet(depart, arrivee);
        List<ReservationBusResponseDTO> responseDTOs = reservationBusMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Réservations bus par trajet récupérées", responseDTOs));
    }

    /**
     * Compte le nombre total de réservations de bus d'un client.

     * GET /api/v1/reservations/bus/client/{clientId}/count
     *
     * @param clientId l'ID du client
     * @return le nombre de réservations (200 OK)
     */
    @GetMapping("/client/{clientId}/count")
    public ResponseEntity<ApiResponse> countReservationsByClient(@PathVariable Integer clientId) {
        long count = reservationBusService.countReservationsByClient(clientId);
        String message = String.format("Nombre de réservations bus : %d", count);

        return ResponseEntity.ok(ApiResponse.success(message, count));
    }

    // ===========================================================
    // ==========        ACTIONS MÉTIER                     ======
    // ===========================================================

    /**
     * Confirme une réservation de bus (change le statut à CONFIRMEE).

     * PATCH /api/v1/reservations/bus/{id}/confirmer
     *
     * @param id l'ID de la réservation à confirmer
     * @return la réservation confirmée (200 OK)
     */
    @PatchMapping("/{id}/confirmer")
    public ResponseEntity<ApiResponse> confirmerReservation(@PathVariable Integer id) {
        ReservationBus reservation = reservationBusService.confirmerReservation(id);
        ReservationBusResponseDTO responseDTO = reservationBusMapper.toResponseDTO(reservation);

        return ResponseEntity.ok(
                ApiResponse.success("Réservation de bus confirmée avec succès", responseDTO));
    }

    /**
     * Annule une réservation de bus (change le statut à ANNULEE).

     * PATCH /api/v1/reservations/bus/{id}/annuler
     *
     * @param id l'ID de la réservation à annuler
     * @return la réservation annulée (200 OK)
     */
    @PatchMapping("/{id}/annuler")
    public ResponseEntity<ApiResponse> annulerReservation(@PathVariable Integer id) {
        ReservationBus reservation = reservationBusService.annulerReservation(id);
        ReservationBusResponseDTO responseDTO = reservationBusMapper.toResponseDTO(reservation);

        return ResponseEntity.ok(
                ApiResponse.success("Réservation de bus annulée avec succès", responseDTO));
    }

    // ===========================================================
    // ==========        RECHERCHES SPÉCIFIQUES BUS         ======
    // ===========================================================

    /**
     * Recherche les réservations par compagnie de bus.

     * GET /api/v1/reservations/bus/compagnie/{compagnie}
     *
     * @param compagnie le nom de la compagnie (ex: "Touristique Express")
     * @return liste des réservations (200 OK)
     */
    @GetMapping("/compagnie/{compagnie}")
    public ResponseEntity<ApiResponse> getReservationsParCompagnie(@PathVariable String compagnie) {
        List<ReservationBus> reservations = reservationBusService.findByCompagnieBus(compagnie);
        List<ReservationBusResponseDTO> responseDTOs = reservationBusMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Réservations par compagnie récupérées", responseDTOs));
    }

    /**
     * Recherche les réservations par type de bus.

     * GET /api/v1/reservations/bus/type/{typeBus}
     *
     * @param typeBus le type de bus (STANDARD ou VIP)
     * @return liste des réservations (200 OK)
     */
    @GetMapping("/type/{typeBus}")
    public ResponseEntity<ApiResponse> getReservationsParType(@PathVariable TypeBus typeBus) {
        List<ReservationBus> reservations = reservationBusService.findByTypeBus(typeBus);
        List<ReservationBusResponseDTO> responseDTOs = reservationBusMapper.toResponseDTOList(reservations);

        String message = String.format("Réservations %s récupérées", typeBus);
        return ResponseEntity.ok(ApiResponse.success(message, responseDTOs));
    }

    /**
     * Recherche les réservations avec ou sans climatisation.

     * GET /api/v1/reservations/bus/climatisation/{hasClim}
     *
     * @param hasClim true pour avec clim, false pour sans clim
     * @return liste des réservations (200 OK)
     */
    @GetMapping("/climatisation/{hasClim}")
    public ResponseEntity<ApiResponse> getReservationsParClimatisation(@PathVariable Boolean hasClim) {
        List<ReservationBus> reservations = reservationBusService.findByClimatisation(hasClim);
        List<ReservationBusResponseDTO> responseDTOs = reservationBusMapper.toResponseDTOList(reservations);

        String message = hasClim ? "Réservations avec climatisation" : "Réservations sans climatisation";
        return ResponseEntity.ok(ApiResponse.success(message, responseDTOs));
    }

    /**
     * Compte le nombre de réservations pour une compagnie donnée.

     * GET /api/v1/reservations/bus/compagnie/{compagnie}/count
     *
     * @param compagnie le nom de la compagnie
     * @return le nombre de réservations (200 OK)
     */
    @GetMapping("/compagnie/{compagnie}/count")
    public ResponseEntity<ApiResponse> countByCompagnie(@PathVariable String compagnie) {
        long count = reservationBusService.countByCompagnieBus(compagnie);
        String message = String.format("Nombre de réservations pour %s : %d", compagnie, count);

        return ResponseEntity.ok(ApiResponse.success(message, count));
    }
}