package com.logondigital.bozacm.controller.reservation;

import com.logondigital.bozacm.DTO.common.ApiResponse;
import com.logondigital.bozacm.DTO.mapper.reservation.ReservationAvionMapper;
import com.logondigital.bozacm.DTO.reservation.avion.ReservationAvionRequestDTO;
import com.logondigital.bozacm.DTO.reservation.avion.ReservationAvionResponseDTO;
import com.logondigital.bozacm.DTO.reservation.avion.ReservationAvionUpdateDTO;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.entities.reservation.ReservationAvion;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.ClasseAvion;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.OffreRepository;
import com.logondigital.bozacm.service.client.ClientService;
import com.logondigital.bozacm.service.reservation.ReservationAvionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST pour gérer les réservations d'AVION.
 * Base URL : /api/v1/reservations/avion
 */
@RestController
@RequestMapping("/api/v1/reservations/avion")
@RequiredArgsConstructor
public class ReservationAvionController {

    private final ReservationAvionService reservationAvionService;
    private final ClientService clientService;
    private final ReservationAvionMapper reservationAvionMapper;
    private final OffreRepository offreRepository;

    // ==================== CRUD DE BASE ====================

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createReservation(@Valid @RequestBody ReservationAvionRequestDTO requestDTO) {

        // 1. Récupérer le client
        Client client = this.clientService.getClientById(requestDTO.getClientId());

        // 2. Récupérer l'offre (source du trajet, prix, date)
        Offre offre = this.offreRepository.findById(requestDTO.getOffreId())
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Offre introuvable avec l'id : " + requestDTO.getOffreId()));

        // 3. Convertir DTO → Entity (avec l'offre)
        ReservationAvion reservation = this.reservationAvionMapper.toEntity(requestDTO, client, offre);

        // 4. Créer la réservation
        ReservationAvion reservationCreee = this.reservationAvionService.createReservation(reservation);

        // 5. Convertir Entity → DTO
        ReservationAvionResponseDTO responseDTO = this.reservationAvionMapper.toResponseDTO(reservationCreee);

        // 6. Retourner ApiResponse
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Réservation d'avion créée avec succès", responseDTO));
    }

    @GetMapping("/get_all")
    public ResponseEntity<ApiResponse> getAllReservations() {
        List<ReservationAvion> reservations = this.reservationAvionService.getAllReservations();
        List<ReservationAvionResponseDTO> responseDTOs = this.reservationAvionMapper.toResponseDTOList(reservations);

        String message = String.format("%d réservation(s) d'avion récupérée(s)", responseDTOs.size());
        return ResponseEntity.ok(ApiResponse.success(message, responseDTOs));
    }

    @GetMapping("/get_reservation_by_id/{id}")
    public ResponseEntity<ApiResponse> getReservationById(@PathVariable Integer id) {
        ReservationAvion reservation = this.reservationAvionService.getReservationById(id);
        ReservationAvionResponseDTO responseDTO = this.reservationAvionMapper.toResponseDTO(reservation);

        return ResponseEntity.ok(
                ApiResponse.success("Réservation d'avion récupérée avec succès", responseDTO));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateReservation(@PathVariable Integer id, @Valid @RequestBody ReservationAvionUpdateDTO updateDTO) {

        ReservationAvion reservation = this.reservationAvionService.getReservationById(id);
        this.reservationAvionMapper.updateEntityFromDTO(reservation, updateDTO);
        ReservationAvion reservationMiseAJour = this.reservationAvionService.updateReservation(id, reservation);
        ReservationAvionResponseDTO responseDTO = this.reservationAvionMapper.toResponseDTO(reservationMiseAJour);

        return ResponseEntity.ok(
                ApiResponse.success("Réservation d'avion mise à jour avec succès", responseDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteReservation(@PathVariable Integer id) {
        this.reservationAvionService.deleteReservation(id);
        return ResponseEntity.ok(ApiResponse.deleted("Réservation d'avion supprimée avec succès"));
    }

    // ==================== HISTORIQUE CLIENT ====================

    @GetMapping("/client/{clientId}/historique")
    public ResponseEntity<ApiResponse> getHistoriqueComplet(@PathVariable Integer clientId) {
        List<ReservationAvion> reservations = this.reservationAvionService.getHistoriqueComplet(clientId);
        List<ReservationAvionResponseDTO> responseDTOs = this.reservationAvionMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Historique complet des réservations avion récupéré", responseDTOs));
    }

    @GetMapping("/client/{clientId}/historique/passes")
    public ResponseEntity<ApiResponse> getHistoriquePasse(@PathVariable Integer clientId) {
        List<ReservationAvion> reservations = this.reservationAvionService.getHistoriquePasse(clientId);
        List<ReservationAvionResponseDTO> responseDTOs = this.reservationAvionMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Historique passé des réservations avion récupéré", responseDTOs));
    }

    @GetMapping("/client/{clientId}/a-venir")
    public ResponseEntity<ApiResponse> getReservationsAVenir(@PathVariable Integer clientId) {
        List<ReservationAvion> reservations = this.reservationAvionService.getReservationsAVenir(clientId);
        List<ReservationAvionResponseDTO> responseDTOs = this.reservationAvionMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Réservations avion à venir récupérées", responseDTOs));
    }

    // ==================== FILTRAGE ====================

    @GetMapping("/client/{clientId}/statut/{statut}")
    public ResponseEntity<ApiResponse> getReservationsParStatut(
            @PathVariable Integer clientId,
            @PathVariable StatutReservation statut) {

        List<ReservationAvion> reservations = this.reservationAvionService.getReservationsParStatut(clientId, statut);
        List<ReservationAvionResponseDTO> responseDTOs = this.reservationAvionMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Réservations avion par statut récupérées", responseDTOs));
    }

    @GetMapping("/trajet")
    public ResponseEntity<ApiResponse> getReservationsParTrajet(
            @RequestParam String depart,
            @RequestParam String arrivee) {

        List<ReservationAvion> reservations = this.reservationAvionService.getReservationsParTrajet(depart, arrivee);
        List<ReservationAvionResponseDTO> responseDTOs = this.reservationAvionMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Réservations avion par trajet récupérées", responseDTOs));
    }

    @GetMapping("/client/{clientId}/count")
    public ResponseEntity<ApiResponse> countReservationsByClient(@PathVariable Integer clientId) {
        long count = this.reservationAvionService.countReservationsByClient(clientId);
        String message = String.format("Nombre de réservations avion : %d", count);

        return ResponseEntity.ok(ApiResponse.success(message, count));
    }

    // ==================== ACTIONS MÉTIER ====================

    @PatchMapping("/{id}/confirmer")
    public ResponseEntity<ApiResponse> confirmerReservation(@PathVariable Integer id) {
        ReservationAvion reservation = this.reservationAvionService.confirmerReservation(id);
        ReservationAvionResponseDTO responseDTO = this.reservationAvionMapper.toResponseDTO(reservation);

        return ResponseEntity.ok(
                ApiResponse.success("Réservation d'avion confirmée avec succès", responseDTO));
    }

    @PatchMapping("/{id}/annuler")
    public ResponseEntity<ApiResponse> annulerReservation(@PathVariable Integer id) {
        ReservationAvion reservation = this.reservationAvionService.annulerReservation(id);
        ReservationAvionResponseDTO responseDTO = this.reservationAvionMapper.toResponseDTO(reservation);

        return ResponseEntity.ok(
                ApiResponse.success("Réservation d'avion annulée avec succès", responseDTO));
    }

    // ==================== RECHERCHES SPÉCIFIQUES AVION ====================

    @GetMapping("/compagnie/{compagnie}")
    public ResponseEntity<ApiResponse> getReservationsParCompagnie(@PathVariable String compagnie) {
        List<ReservationAvion> reservations = this.reservationAvionService.findByCompagnieAerienne(compagnie);
        List<ReservationAvionResponseDTO> responseDTOs = this.reservationAvionMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Réservations par compagnie récupérées", responseDTOs));
    }

    @GetMapping("/vol/{numeroVol}")
    public ResponseEntity<ApiResponse> getReservationsParVol(@PathVariable String numeroVol) {
        List<ReservationAvion> reservations = this.reservationAvionService.findByNumeroVol(numeroVol);
        List<ReservationAvionResponseDTO> responseDTOs = this.reservationAvionMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Réservations par vol récupérées", responseDTOs));
    }

    @GetMapping("/classe/{classeAvion}")
    public ResponseEntity<ApiResponse> getReservationsParClasse(@PathVariable ClasseAvion classeAvion) {
        List<ReservationAvion> reservations = this.reservationAvionService.findByClasseAvion(classeAvion);
        List<ReservationAvionResponseDTO> responseDTOs = this.reservationAvionMapper.toResponseDTOList(reservations);

        String message = String.format("Réservations classe %s récupérées", classeAvion);
        return ResponseEntity.ok(ApiResponse.success(message, responseDTOs));
    }

    @GetMapping("/terminal/{numeroTerminal}")
    public ResponseEntity<ApiResponse> getReservationsParTerminal(@PathVariable String numeroTerminal) {
        List<ReservationAvion> reservations = this.reservationAvionService.findByNumeroTerminal(numeroTerminal);
        List<ReservationAvionResponseDTO> responseDTOs = this.reservationAvionMapper.toResponseDTOList(reservations);

        return ResponseEntity.ok(
                ApiResponse.success("Réservations par terminal récupérées", responseDTOs));
    }

    @GetMapping("/bagages")
    public ResponseEntity<ApiResponse> getReservationsParPoidsBagages(@RequestParam Integer poidsMin) {
        List<ReservationAvion> reservations = this.reservationAvionService.findByPoidsMaxBagagesGreaterThanEqual(poidsMin);
        List<ReservationAvionResponseDTO> responseDTOs = this.reservationAvionMapper.toResponseDTOList(reservations);

        String message = String.format("Réservations avec bagages >= %d kg", poidsMin);
        return ResponseEntity.ok(ApiResponse.success(message, responseDTOs));
    }

    @GetMapping("/compagnie/{compagnie}/count")
    public ResponseEntity<ApiResponse> countByCompagnie(@PathVariable String compagnie) {
        long count = this.reservationAvionService.countByCompagnieAerienne(compagnie);
        String message = String.format("Nombre de réservations pour %s : %d", compagnie, count);

        return ResponseEntity.ok(ApiResponse.success(message, count));
    }

    @GetMapping("/vol/{numeroVol}/count")
    public ResponseEntity<ApiResponse> countByVol(@PathVariable String numeroVol) {
        long count = this.reservationAvionService.countByNumeroVol(numeroVol);
        String message = String.format("Nombre de réservations pour le vol %s : %d", numeroVol, count);

        return ResponseEntity.ok(ApiResponse.success(message, count));
    }

    @GetMapping("/compagnies")
    public ResponseEntity<ApiResponse> getAllCompagnies() {
        List<String> compagnies = this.reservationAvionService.findAllCompagniesDistinctes();

        return ResponseEntity.ok(
                ApiResponse.success("Liste des compagnies récupérée", compagnies));
    }

    @GetMapping("/compagnie/{compagnie}/vols")
    public ResponseEntity<ApiResponse> getVolsByCompagnie(@PathVariable String compagnie) {
        List<String> vols = this.reservationAvionService.findVolsByCompagnie(compagnie);

        return ResponseEntity.ok(
                ApiResponse.success("Liste des vols récupérée", vols));
    }
}