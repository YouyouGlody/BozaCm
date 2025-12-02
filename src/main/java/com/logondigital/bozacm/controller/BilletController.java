package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.dto.billet.BilletRequestDTO;
import com.logondigital.bozacm.dto.billet.BilletResponseDTO;
import com.logondigital.bozacm.dto.common.ApiResponse;
import com.logondigital.bozacm.dto.mapper.BilletMapper;
import com.logondigital.bozacm.entities.Billet;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.entities.reservation.Reservation;
import com.logondigital.bozacm.enums.StatutBillet;
import com.logondigital.bozacm.repository.ClientRepo;
import com.logondigital.bozacm.repository.reservation.ReservationAvionRepo;
import com.logondigital.bozacm.repository.reservation.ReservationBusRepo;
import com.logondigital.bozacm.repository.reservation.ReservationTrainRepo;
import com.logondigital.bozacm.service.billet.BilletService;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

/**
 * Controller REST pour gérer les billets.
 * CHERCHE DANS TOUS LES TYPES DE RÉSERVATIONS !
 */
@RestController
@RequestMapping("/api/v1/billets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BilletController {

    private final BilletService billetService;
    private final BilletMapper billetMapper;
    private final ClientRepo clientRepo;
    private final ReservationBusRepo reservationBusRepo;
    private final ReservationTrainRepo reservationTrainRepo;
    private final ReservationAvionRepo reservationAvionRepo;


    /**
     * Crée un nouveau billet.
     * POST /api/v1/billets/create
     */
    @PostMapping(path = "/create")
    public ResponseEntity<ApiResponse> createBillet(@Valid @RequestBody BilletRequestDTO requestDTO) {
        // 1. Récupérer le client
        Client client = clientRepo.findById(requestDTO.getClientId())
                .orElseThrow(() -> new RessourceNotFoundException("Client introuvable avec l'ID " + requestDTO.getClientId()));

        // 2. Chercher la réservation dans TOUS les types
        Reservation reservation = findReservationById(requestDTO.getReservationId());

        // 3. Utiliser le Mapper
        Billet billet = billetMapper.toEntity(requestDTO, client, reservation);
        Billet billetCree = billetService.createBillet(billet);

        // 4. Convertir en DTO
        BilletResponseDTO responseDTO = billetMapper.toResponseDTO(billetCree);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Billet créé avec succès", responseDTO));
    }

    /**
     * MÉTHODE HELPER : Cherche une réservation dans TOUS les types.
     */
    private Reservation findReservationById(Integer reservationId) {
        // Essayer Bus
        Optional<Reservation> reservation = reservationBusRepo.findById(reservationId)
                .map(r -> (Reservation) r);
        if (reservation.isPresent()) {
            return reservation.get();
        }

        // Essayer Train
        reservation = reservationTrainRepo.findById(reservationId)
                .map(r -> (Reservation) r);
        if (reservation.isPresent()) {
            return reservation.get();
        }

        // Essayer Avion
        reservation = reservationAvionRepo.findById(reservationId)
                .map(r -> (Reservation) r);
        if (reservation.isPresent()) {
            return reservation.get();
        }

        // Si aucune réservation trouvée
        throw new RessourceNotFoundException("Réservation introuvable avec l'ID " + reservationId);
    }

    /**
     * Récupère tous les billets.
     */
    @GetMapping(path = "/get_all")
    public ResponseEntity<ApiResponse> getAllBillets() {
        List<Billet> billets = billetService.getAllBillets();
        List<BilletResponseDTO> responseDTOs = billetMapper.toResponseDTOList(billets);
        return ResponseEntity.ok(ApiResponse.success("Récupération de tous les billets", responseDTOs));
    }

    /**
     * Récupère un billet par ID.
     */
    @GetMapping(path = "/get_by_id/{idBillet}")
    public ResponseEntity<ApiResponse> getBilletById(@PathVariable Integer idBillet) {
        Billet billet = billetService.getBilletById(idBillet);
        BilletResponseDTO responseDTO = billetMapper.toResponseDTO(billet);
        return ResponseEntity.ok(ApiResponse.success("Billet récupéré", responseDTO));
    }

    /**
     * Supprime un billet.
     */
    @DeleteMapping(path = "/delete/{idBillet}")
    public ResponseEntity<ApiResponse> deleteBillet(@PathVariable Integer idBillet) {
        billetService.deleteBilletById(idBillet);
        return ResponseEntity.ok(ApiResponse.deleted("Billet supprimé"));
    }

    /**
     * Compte les billets.
     */
    @GetMapping(path = "/count")
    public ResponseEntity<ApiResponse> countBillets() {
        long count = billetService.countBillets();
        return ResponseEntity.ok(ApiResponse.success("Nombre de billets", count));
    }

    /**
     * Cherche un billet par numéro.
     */
    @GetMapping("/numero/{numeroBillet}")
    public ResponseEntity<ApiResponse> getBilletByNumero(@PathVariable String numeroBillet) {
        Billet billet = billetService.findByNumeroBillet(numeroBillet);
        BilletResponseDTO responseDTO = billetMapper.toResponseDTO(billet);
        return ResponseEntity.ok(ApiResponse.success("Billet trouvé", responseDTO));
    }

    /**
     * Récupère les billets d'un client.
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponse> getBilletsClient(@PathVariable Integer clientId) {
        List<Billet> billets = billetService.getBilletsClient(clientId);
        List<BilletResponseDTO> responseDTOs = billetMapper.toResponseDTOList(billets);
        return ResponseEntity.ok(ApiResponse.success("Billets du client", responseDTOs));
    }

    /**
     * Récupère les billets par statut.
     */
    @GetMapping("/client/{clientId}/statut/{statut}")
    public ResponseEntity<ApiResponse> getBilletsParStatut(@PathVariable Integer clientId, @PathVariable StatutBillet statut) {
        List<Billet> billets = billetService.getBilletsParStatut(clientId, statut);
        List<BilletResponseDTO> responseDTOs = billetMapper.toResponseDTOList(billets);
        return ResponseEntity.ok(ApiResponse.success("Billets par statut", responseDTOs));
    }

    /**
     * Récupère le billet d'une réservation.
     */
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<ApiResponse> getBilletByReservation(@PathVariable Integer reservationId) {
        Billet billet = billetService.getBilletByReservation(reservationId);
        BilletResponseDTO responseDTO = billetMapper.toResponseDTO(billet);
        return ResponseEntity.ok(ApiResponse.success("Billet de la réservation", responseDTO));
    }

    /**
     * Compte les billets d'un client.
     */
    @GetMapping("/client/{clientId}/count")
    public ResponseEntity<ApiResponse> countBilletsByClient(@PathVariable Integer clientId) {
        long count = billetService.countBilletsByClient(clientId);
        return ResponseEntity.ok(ApiResponse.success("Nombre de billets du client", count));
    }

    /**
     * Marque un billet comme utilisé.
     */
    @PatchMapping("/numero/{numeroBillet}/utiliser")
    public ResponseEntity<ApiResponse> marquerBilletUtilise(@PathVariable String numeroBillet) {
        Billet billet = billetService.marquerBilletUtilise(numeroBillet);
        BilletResponseDTO responseDTO = billetMapper.toResponseDTO(billet);
        return ResponseEntity.ok(ApiResponse.success("Billet utilisé", responseDTO));
    }

    /**
     * Expire les billets périmés.
     */
    @PostMapping("/expirer-perimes")
    public ResponseEntity<ApiResponse> expirerBilletsPerimes() {
        billetService.expirerBilletsPerimes();
        return ResponseEntity.ok(ApiResponse.success("Billets expirés"));
    }



}

