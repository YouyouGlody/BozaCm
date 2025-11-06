package com.logondigital.bozacm.controller.reservation;

import com.logondigital.bozacm.dto.common.ApiResponse;
import com.logondigital.bozacm.dto.mapper.reservation.ReservationTrainMapper;
import com.logondigital.bozacm.dto.reservation.train.ReservationTrainRequestDTO;
import com.logondigital.bozacm.dto.reservation.train.ReservationTrainResponseDTO;
import com.logondigital.bozacm.dto.reservation.train.ReservationTrainUpdateDTO;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.entities.reservation.ReservationTrain;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.ClasseTrain;
import com.logondigital.bozacm.service.client.ClientService;
import com.logondigital.bozacm.service.reservation.ReservationTrainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST pour gérer les réservations de TRAIN.

 * Base URL : /api/v1/reservations/train
 */
@RestController
@RequestMapping("/api/v1/reservations/train")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReservationTrainController {

    private final ReservationTrainService reservationTrainService;
    private final ClientService clientService;
    private final ReservationTrainMapper reservationTrainMapper;

    // ==================== CRUD DE BASE ====================

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createReservation(@Valid @RequestBody ReservationTrainRequestDTO requestDTO) {

        // 1. Récupérer la reservation client
        Client client = this.clientService.getClientById(requestDTO.getClientId());

        // 2. Convertir DTO → Entity
        ReservationTrain reservation = this.reservationTrainMapper.toEntity(requestDTO, client);

        // 3. Créer la réservation
        ReservationTrain reservationCreee = this.reservationTrainService.createReservation(reservation);

        // 4. Convertir Entity → DTO
        ReservationTrainResponseDTO responseDTO = this.reservationTrainMapper.toResponseDTO(reservationCreee);

        // 5. Retourner ApiResponse
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Réservation de train créée avec succès", responseDTO));
    }

    @GetMapping("/get_all" )
    public ResponseEntity<ApiResponse> getAllReservations() {
        List<ReservationTrain> reservations = this.reservationTrainService.getAllReservations();
        List<ReservationTrainResponseDTO> responseDTOs = this.reservationTrainMapper.toResponseDTOList(reservations);

        String message = String.format("%d réservation(s) de train récupérée(s)", responseDTOs.size());
        return ResponseEntity.ok(ApiResponse.success(message, responseDTOs));
    }

    @GetMapping("/get_reservation_by_id/{id}")
    public ResponseEntity<ApiResponse> getReservationById(@PathVariable Integer id) {
        ReservationTrain reservation = this.reservationTrainService.getReservationById(id);
        ReservationTrainResponseDTO responseDTO = this.reservationTrainMapper.toResponseDTO(reservation);

        return ResponseEntity.ok(
                ApiResponse.success("Réservation de train récupérée avec succès", responseDTO));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateReservation(@PathVariable Integer id, @Valid @RequestBody ReservationTrainUpdateDTO updateDTO) {

        // 1. Récupérer la réservation existante
        ReservationTrain reservation = this.reservationTrainService.getReservationById(id);

        // 2. Appliquer les modifications
        this.reservationTrainMapper.updateEntityFromDTO(reservation, updateDTO);

        // 3. Sauvegarder
        ReservationTrain reservationMiseAJour = this.reservationTrainService.updateReservation(id, reservation);

        // 4. Convertir Entity → DTO
        ReservationTrainResponseDTO responseDTO = this.reservationTrainMapper.toResponseDTO(reservationMiseAJour);

        // 5. Retourner ApiResponse
        return ResponseEntity.ok(
                ApiResponse.success("Réservation de train mise à jour avec succès", responseDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteReservation(@PathVariable Integer id) {
        this.reservationTrainService.deleteReservation(id);
        return ResponseEntity.ok(ApiResponse.deleted("Réservation de train supprimée avec succès"));
    }

    // ==================== HISTORIQUE CLIENT ====================

    @GetMapping("/client/{clientId}/historique")
    public ResponseEntity<ApiResponse> getHistoriqueComplet(@PathVariable Integer clientId) {
        List<ReservationTrain> reservations =  this.reservationTrainService.getHistoriqueComplet(clientId);
        List<ReservationTrainResponseDTO> responseDTOs =  this.reservationTrainMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Historique complet des réservations train récupéré", responseDTOs));
    }

    @GetMapping("/client/{clientId}/historique/passes")
    public ResponseEntity<ApiResponse> getHistoriquePasse(@PathVariable Integer clientId) {
        List<ReservationTrain> reservations =  this.reservationTrainService.getHistoriquePasse(clientId);
        List<ReservationTrainResponseDTO> responseDTOs =  this.reservationTrainMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Historique passé des réservations train récupéré", responseDTOs));
    }

    @GetMapping("/client/{clientId}/a-venir")
    public ResponseEntity<ApiResponse> getReservationsAVenir(@PathVariable Integer clientId) {
        List<ReservationTrain> reservations =  this.reservationTrainService.getReservationsAVenir(clientId);
        List<ReservationTrainResponseDTO> responseDTOs = reservationTrainMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Réservations train à venir récupérées", responseDTOs));
    }

    // ==================== FILTRAGE ====================

    @GetMapping("/client/{clientId}/statut/{statut}")
    public ResponseEntity<ApiResponse> getReservationsParStatut(@PathVariable Integer clientId, @PathVariable StatutReservation statut) {

        List<ReservationTrain> reservations =  this.reservationTrainService.getReservationsParStatut(clientId, statut);

        List<ReservationTrainResponseDTO> responseDTOs =  this.reservationTrainMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Réservations train par statut récupérées", responseDTOs));
    }

    @GetMapping("/trajet")
    public ResponseEntity<ApiResponse> getReservationsParTrajet(@RequestParam String depart, @RequestParam String arrivee) {

        List<ReservationTrain> reservations =  this.reservationTrainService.getReservationsParTrajet(depart, arrivee);

        List<ReservationTrainResponseDTO> responseDTOs =  this.reservationTrainMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Réservations train par trajet récupérées", responseDTOs));
    }

    @GetMapping("/client/{clientId}/count")
    public ResponseEntity<ApiResponse> countReservationsByClient(@PathVariable Integer clientId) {
        long count =  this.reservationTrainService.countReservationsByClient(clientId);
        String message = String.format("Nombre de réservations train : %d", count);

        return ResponseEntity.ok(ApiResponse.success(message, count));
    }

    // ==================== ACTIONS MÉTIER ====================

    @PatchMapping("/{id}/confirmer")
    public ResponseEntity<ApiResponse> confirmerReservation(@PathVariable Integer id) {
        ReservationTrain reservation =  this.reservationTrainService.confirmerReservation(id);

        ReservationTrainResponseDTO responseDTO =  this.reservationTrainMapper.toResponseDTO(reservation);

        return ResponseEntity.ok(
                ApiResponse.success("Réservation de train confirmée avec succès", responseDTO));
    }

    @PatchMapping("/{id}/annuler")
    public ResponseEntity<ApiResponse> annulerReservation(@PathVariable Integer id) {
        ReservationTrain reservation =  this.reservationTrainService.annulerReservation(id);
        ReservationTrainResponseDTO responseDTO =  this.reservationTrainMapper.toResponseDTO(reservation);

        return ResponseEntity.ok(
                ApiResponse.success("Réservation de train annulée avec succès", responseDTO));
    }

    // ==================== RECHERCHES SPÉCIFIQUES TRAIN ====================

    /**
     * Recherche par compagnie de train.
     * GET /api/v1/reservations/train/compagnie/{compagnie}
     */
    @GetMapping("/compagnie/{compagnie}")
    public ResponseEntity<ApiResponse> getReservationsParCompagnie(@PathVariable String compagnie) {
        List<ReservationTrain> reservations = reservationTrainService.findByCompagnieTrain(compagnie);
        List<ReservationTrainResponseDTO> responseDTOs = reservationTrainMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Réservations par compagnie récupérées", responseDTOs));
    }

    /**
     * Recherche par numéro de wagon.
     * GET /api/v1/reservations/train/wagon/{numeroWagon}
     */
    @GetMapping("/wagon/{numeroWagon}")
    public ResponseEntity<ApiResponse> getReservationsParWagon(@PathVariable String numeroWagon) {
        List<ReservationTrain> reservations = reservationTrainService.findByNumeroWagon(numeroWagon);
        List<ReservationTrainResponseDTO> responseDTOs = reservationTrainMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Réservations par wagon récupérées", responseDTOs));
    }

    /**
     * Recherche par classe de train.
     * GET /api/v1/reservations/train/classe/{classeTrain}
     */
    @GetMapping("/classe/{classeTrain}")
    public ResponseEntity<ApiResponse> getReservationsParClasse(@PathVariable ClasseTrain classeTrain) {
        List<ReservationTrain> reservations = reservationTrainService.findByClasseTrain(classeTrain);
        List<ReservationTrainResponseDTO> responseDTOs = reservationTrainMapper.toResponseDTOList(reservations);

        String message = String.format("Réservations classe %s récupérées", classeTrain);
        return ResponseEntity.ok(ApiResponse.success(message, responseDTOs));
    }

    /**
     * Compte par compagnie.
     * GET /api/v1/reservations/train/compagnie/{compagnie}/count
     */
    @GetMapping("/compagnie/{compagnie}/count")
    public ResponseEntity<ApiResponse> countByCompagnie(@PathVariable String compagnie) {
        long count = reservationTrainService.countByCompagnieTrain(compagnie);
        String message = String.format("Nombre de réservations pour %s : %d", compagnie, count);

        return ResponseEntity.ok(ApiResponse.success(message, count));
    }

    /**
     * Liste des compagnies distinctes.
     * GET /api/v1/reservations/train/compagnies
     */
    @GetMapping("/compagnies")
    public ResponseEntity<ApiResponse> getAllCompagnies() {
        List<String> compagnies = reservationTrainService.findAllCompagniesDistinctes();

        return ResponseEntity.ok(
                ApiResponse.success("Liste des compagnies récupérée", compagnies));
    }
}