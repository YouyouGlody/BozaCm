package com.logondigital.bozacm.controller.reservation;

import com.logondigital.bozacm.DTO.common.ApiResponse;
import com.logondigital.bozacm.DTO.mapper.reservation.ReservationBusMapper;
import com.logondigital.bozacm.DTO.reservation.bus.ReservationBusRequestDTO;
import com.logondigital.bozacm.DTO.reservation.bus.ReservationBusResponseDTO;
import com.logondigital.bozacm.DTO.reservation.bus.ReservationBusUpdateDTO;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.entities.reservation.ReservationBus;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.TypeBus;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.OffreRepository;
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
 * Base URL : /api/v1/reservations/bus
 */
@RestController
@RequestMapping("/api/v1/reservations/bus")
@RequiredArgsConstructor
public class ReservationBusController {

    private final ReservationBusService reservationBusService;
    private final ClientService clientService;
    private final ReservationBusMapper reservationBusMapper;
    private final OffreRepository offreRepository;

    // ===========================================================
    // ==========        CRUD DE BASE                       ======
    // ===========================================================

    /**
     * Crée une nouvelle réservation de bus.
     * Le client choisit une offre (offreId) qui fournit trajet, prix et date.
     * POST /api/v1/reservations/bus/create
     */
    @PostMapping(path = "/create")
    public ResponseEntity<ApiResponse> createReservation(
            @Valid @RequestBody ReservationBusRequestDTO requestDTO) {

        // 1. Récupérer le client
        Client client = this.clientService.getClientById(requestDTO.getClientId());

        // 2. Récupérer l'offre (source du trajet, prix, date)
        Offre offre = this.offreRepository.findById(requestDTO.getOffreId())
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Offre introuvable avec l'id : " + requestDTO.getOffreId()));

        // 3. Convertir DTO → Entity (avec l'offre)
        ReservationBus reservation = this.reservationBusMapper.toEntity(requestDTO, client, offre);

        // 4. Créer la réservation
        ReservationBus reservationCreee = this.reservationBusService.createReservation(reservation);

        // 5. Convertir Entity → DTO
        ReservationBusResponseDTO responseDTO = this.reservationBusMapper.toResponseDTO(reservationCreee);

        // 6. Retourner ApiResponse
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Réservation de bus créée avec succès", responseDTO));
    }

    /**
     * Récupère toutes les réservations de bus.
     * GET /api/v1/reservations/bus/get_all
     */
    @GetMapping(path = "/get_all")
    public ResponseEntity<ApiResponse> getAllReservations() {
        List<ReservationBus> reservations = this.reservationBusService.getAllReservations();
        List<ReservationBusResponseDTO> responseDTOs = this.reservationBusMapper.toResponseDTOList(reservations);

        String message = String.format("%d réservation(s) de bus récupérée(s)", responseDTOs.size());
        return ResponseEntity.ok(ApiResponse.success(message, responseDTOs));
    }

    /**
     * Récupère une réservation de bus par son ID.
     * GET /api/v1/reservations/bus/get_reservation_by_id/{id}
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
     * PUT /api/v1/reservations/bus/update/{id}
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateReservation(@PathVariable Integer id, @Valid @RequestBody ReservationBusUpdateDTO updateDTO) {

        ReservationBus reservation = this.reservationBusService.getReservationById(id);
        this.reservationBusMapper.updateEntityFromDTO(reservation, updateDTO);
        ReservationBus reservationMiseAJour = this.reservationBusService.updateReservation(id, reservation);
        ReservationBusResponseDTO responseDTO = this.reservationBusMapper.toResponseDTO(reservationMiseAJour);

        return ResponseEntity.ok(
                ApiResponse.success("Réservation de bus mise à jour avec succès", responseDTO));
    }

    /**
     * Supprime une réservation de bus par son ID.
     * DELETE /api/v1/reservations/bus/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteReservation(@PathVariable Integer id) {
        reservationBusService.deleteReservation(id);
        return ResponseEntity.ok(ApiResponse.deleted("Réservation de bus supprimée avec succès"));
    }

    // ===========================================================
    // ==========        HISTORIQUE CLIENT                  ======
    // ===========================================================

    @GetMapping("/client/{clientId}/historique")
    public ResponseEntity<ApiResponse> getHistoriqueComplet(@PathVariable Integer clientId) {
        List<ReservationBus> reservations = reservationBusService.getHistoriqueComplet(clientId);
        List<ReservationBusResponseDTO> responseDTOs = reservationBusMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Historique complet des réservations bus récupéré", responseDTOs));
    }

    @GetMapping("/client/{clientId}/historique/passes")
    public ResponseEntity<ApiResponse> getHistoriquePasse(@PathVariable Integer clientId) {
        List<ReservationBus> reservations = reservationBusService.getHistoriquePasse(clientId);
        List<ReservationBusResponseDTO> responseDTOs = reservationBusMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Historique passé des réservations bus récupéré", responseDTOs));
    }

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

    @GetMapping("/client/{clientId}/statut/{statut}")
    public ResponseEntity<ApiResponse> getReservationsParStatut(@PathVariable Integer clientId, @PathVariable StatutReservation statut) {

        List<ReservationBus> reservations = reservationBusService.getReservationsParStatut(clientId, statut);
        List<ReservationBusResponseDTO> responseDTOs = reservationBusMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Réservations bus par statut récupérées", responseDTOs));
    }

    @GetMapping("/trajet")
    public ResponseEntity<ApiResponse> getReservationsParTrajet(@RequestParam String depart, @RequestParam String arrivee) {

        List<ReservationBus> reservations = reservationBusService.getReservationsParTrajet(depart, arrivee);
        List<ReservationBusResponseDTO> responseDTOs = reservationBusMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Réservations bus par trajet récupérées", responseDTOs));
    }

    @GetMapping("/client/{clientId}/count")
    public ResponseEntity<ApiResponse> countReservationsByClient(@PathVariable Integer clientId) {
        long count = reservationBusService.countReservationsByClient(clientId);
        String message = String.format("Nombre de réservations bus : %d", count);

        return ResponseEntity.ok(ApiResponse.success(message, count));
    }

    // ===========================================================
    // ==========        ACTIONS MÉTIER                     ======
    // ===========================================================

    @PatchMapping("/{id}/confirmer")
    public ResponseEntity<ApiResponse> confirmerReservation(@PathVariable Integer id) {
        ReservationBus reservation = reservationBusService.confirmerReservation(id);
        ReservationBusResponseDTO responseDTO = reservationBusMapper.toResponseDTO(reservation);

        return ResponseEntity.ok(
                ApiResponse.success("Réservation de bus confirmée avec succès", responseDTO));
    }

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

    @GetMapping("/compagnie/{compagnie}")
    public ResponseEntity<ApiResponse> getReservationsParCompagnie(@PathVariable String compagnie) {
        List<ReservationBus> reservations = reservationBusService.findByCompagnieBus(compagnie);
        List<ReservationBusResponseDTO> responseDTOs = reservationBusMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Réservations par compagnie récupérées", responseDTOs));
    }

    @GetMapping("/type/{typeBus}")
    public ResponseEntity<ApiResponse> getReservationsParType(@PathVariable TypeBus typeBus) {
        List<ReservationBus> reservations = reservationBusService.findByTypeBus(typeBus);
        List<ReservationBusResponseDTO> responseDTOs = reservationBusMapper.toResponseDTOList(reservations);

        String message = String.format("Réservations %s récupérées", typeBus);
        return ResponseEntity.ok(ApiResponse.success(message, responseDTOs));
    }

    @GetMapping("/climatisation/{hasClim}")
    public ResponseEntity<ApiResponse> getReservationsParClimatisation(@PathVariable Boolean hasClim) {
        List<ReservationBus> reservations = reservationBusService.findByClimatisation(hasClim);
        List<ReservationBusResponseDTO> responseDTOs = reservationBusMapper.toResponseDTOList(reservations);

        String message = hasClim ? "Réservations avec climatisation" : "Réservations sans climatisation";
        return ResponseEntity.ok(ApiResponse.success(message, responseDTOs));
    }

    @GetMapping("/compagnie/{compagnie}/count")
    public ResponseEntity<ApiResponse> countByCompagnie(@PathVariable String compagnie) {
        long count = reservationBusService.countByCompagnieBus(compagnie);
        String message = String.format("Nombre de réservations pour %s : %d", compagnie, count);

        return ResponseEntity.ok(ApiResponse.success(message, count));
    }
}