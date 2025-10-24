package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.dto.client.ClientRequestDTO;
import com.logondigital.bozacm.dto.client.ClientResponseDTO;
import com.logondigital.bozacm.dto.client.ClientUpdateDTO;
import com.logondigital.bozacm.dto.common.ApiResponse;
import com.logondigital.bozacm.dto.mapper.ClientMapper;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.service.client.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Un Controller est la porte d'entrée de ton API REST.
 * Il reçoit les requêtes HTTP (GET, POST, PUT, DELETE) du frontend et appelle les Services pour traiter la logique métier.

 * Controller REST pour gérer les opérations CRUD sur les clients.

 * Ce controller expose les endpoints de l'API pour :
 * - Créer, lire, mettre à jour et supprimer des clients (CRUD)
 * - Rechercher des clients par email ou numéro de téléphone
 * - Compter le nombre total de clients.

 * Base URL : /api/v1/clients

 * @RestController : Combine @Controller + @ResponseBody
 *                   Toutes les méthodes retournent directement du JSON

 * @RequestMapping : Définit l'URL de base pour tous les endpoints de ce controller
 *                   Exemple : /api/v1/clients

 * @RequiredArgsConstructor → Moins de code
 * Retourner l'objet → Standard REST, plus utile pour le frontend

 * Controller REST pour gérer les opérations CRUD sur les clients.
 *  Utilise des DTOs pour séparer la couche API de la couche métier.
 */
@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    /**
     * Service contenant la logique métier des clients.
     * Injection par constructeur (recommandé pour l'immutabilité).
     */
    private final ClientService clientService;
    private final ClientMapper clientMapper;  // ← AJOUTÉ




    // ========================================================================
    // ==========              CRUD DE BASE                          ==========
    // ========================================================================


    /**
     * Crée un nouveau client.

     * POST /api/v1/clients
     *
     * @param requestDTO les données du client (validées)
     * @return ApiResponse avec ClientResponseDTO (201 Created)
     */
    @PostMapping(path = "/create")
    public ResponseEntity<ApiResponse> createClient(@Valid @RequestBody ClientRequestDTO requestDTO) {
        // 1. Convertir DTO → Entity
        Client client = this.clientMapper.toEntity(requestDTO);

        // 2. Appeler le service
        Client clientCree = this.clientService.createClient(client);

        // 3. Convertir Entity → DTO
        ClientResponseDTO responseDTO =  this.clientMapper.toResponseDTO(clientCree);

        // 4. Retourner ApiResponse
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Client créé avec succès ", responseDTO));
    }


    /**
     * Récupère tous les clients.

     * GET /api/v1/clients
     *
     * @return ApiResponse avec List<ClientResponseDTO> (200 OK)
     */
    @GetMapping(path ="/get_all" )
    public ResponseEntity<ApiResponse> getAllClients() {
        // 1. Récupérer tous les clients
        List<Client> clients = this.clientService.getAllClients();

        // 2. Convertir chaque Client en ClientResponseDTO
        List<ClientResponseDTO> responseDTOs = clients.stream()
                .map(clientMapper::toResponseDTO)
                .collect(Collectors.toList());

        // 3. Message dynamique
        String message = String.format("%d client(s) récupéré(s)", responseDTOs.size());

        // 4. Retourner ApiResponse
        return ResponseEntity.ok(ApiResponse.success(message, responseDTOs));
    }


    /**
     * Récupère un client par son ID.

     * GET /api/v1/clients/{idClient}
     *
     * @param idClient l'ID du client
     * @return ApiResponse avec ClientResponseDTO (200 OK)
     */
    @GetMapping(path = "/get_by_id/{idClient}" )
    public ResponseEntity<ApiResponse> getClientById(@PathVariable Integer idClient) {
        // 1. Récupérer le client
        Client client = this.clientService.getClientById(idClient);

        // 2. Convertir Entity → DTO
        ClientResponseDTO responseDTO = this.clientMapper.toResponseDTO(client);

        // 3. Retourner ApiResponse
        return ResponseEntity.ok(ApiResponse.success("Client récupéré avec succès", responseDTO));
    }




    /**
     * Met à jour un client existant.

     * PUT /api/v1/clients/{idClient}
     *
     * @param idClient l'ID du client à modifier
     * @param updateDTO les nouvelles données (champs null = pas de changement)
     * @return ApiResponse avec ClientResponseDTO (200 OK)
     */
    @PutMapping(path = "/update/{idClient}")
    public ResponseEntity<ApiResponse> updateClient(@PathVariable Integer idClient, @RequestBody ClientUpdateDTO updateDTO) {
        // 1. Récupérer le client existant
        Client client =  this.clientService.getClientById(idClient);

        // 2. Mettre à jour avec les données du DTO (update partielle)
        this.clientMapper.updateEntityFromDTO(client, updateDTO);

        // 3. Sauvegarder via le service
        Client clientMisAJour =  this.clientService.updateClient(idClient, client);

        // 4. Convertir Entity → DTO
        ClientResponseDTO responseDTO =  this.clientMapper.toResponseDTO(clientMisAJour);

        // 5. Retourner ApiResponse
        return ResponseEntity.ok(ApiResponse.success("Client mis à jour avec succès", responseDTO));
    }



    /**
     * Supprime un client par son ID.

     * DELETE /api/v1/clients/{idClient}
     *
     * @param idClient l'ID du client à supprimer
     * @return ApiResponse sans données (200 OK)
     */
    @DeleteMapping("/{idClient}")
    public ResponseEntity<ApiResponse> deleteClient(@PathVariable Integer idClient) {
        // Supprime le client
        this.clientService.deleteClient(idClient);

        // Retourner ApiResponse avec data = null
        return ResponseEntity.ok(ApiResponse.deleted("Client supprimé avec succès"));

    }


    // ========================================================================
    // ==========              MÉTHODES MÉTIER                       ==========
    // ========================================================================


    /**
     * Recherche un client par email.

     * GET /api/v1/clients/email/{email}
     *
     * @param email l'email du client
     * @return ApiResponse avec ClientResponseDTO (200 OK)
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse> getClientByEmail(@PathVariable String email) {
        Client client = this.clientService.findByEmail(email);
        ClientResponseDTO responseDTO = this.clientMapper.toResponseDTO(client);

        return ResponseEntity.ok(ApiResponse.success("Client trouvé", responseDTO));
    }


    /**
     * Recherche un client par numéro de téléphone.

     * GET /api/v1/clients/telephone/{numeroTelephone}
     *
     * @param numeroTelephone le numéro de téléphone
     * @return ApiResponse avec ClientResponseDTO (200 OK)
     */
    @GetMapping("/telephone/{numeroTelephone}")
    public ResponseEntity<ApiResponse> getClientByTelephone(@PathVariable String numeroTelephone) {
        Client client = this.clientService.findByNumeroTelephone(numeroTelephone);
        ClientResponseDTO responseDTO =  this.clientMapper.toResponseDTO(client);

        return ResponseEntity.ok(ApiResponse.success("Client trouvé",  responseDTO));
    }

    /**
     * Compte le nombre total de clients.

     * GET /api/v1/clients/count
     *
     * @return ApiResponse avec le comptage (200 OK)
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse> countClients() {
        long count = this.clientService.countClients();
        String message = String.format("Nombre total de clients : %d", count);
        
        return ResponseEntity.ok(ApiResponse.success(message, count));
    }

}
