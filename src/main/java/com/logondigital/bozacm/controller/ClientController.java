package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.DTO.client.ChangePasswordDTO;
import com.logondigital.bozacm.DTO.client.ClientRequestDTO;
import com.logondigital.bozacm.DTO.client.ClientResponseDTO;
import com.logondigital.bozacm.DTO.client.ClientUpdateDTO;
import com.logondigital.bozacm.DTO.common.ApiResponse;
import com.logondigital.bozacm.DTO.mapper.ClientMapper;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.service.client.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**


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


    @GetMapping(path = "/get_by_id/{idClient}" )
    public ResponseEntity<ApiResponse> getClientById(@PathVariable Integer idClient) {
        // 1. Récupérer le client
        Client client = this.clientService.getClientById(idClient);

        // 2. Convertir Entity → DTO
        ClientResponseDTO responseDTO = this.clientMapper.toResponseDTO(client);

        // 3. Retourner ApiResponse
        return ResponseEntity.ok(ApiResponse.success("Client récupéré avec succès", responseDTO));
    }




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


    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse> getClientByEmail(@PathVariable String email) {
        Client client = this.clientService.findByEmail(email);
        ClientResponseDTO responseDTO = this.clientMapper.toResponseDTO(client);

        return ResponseEntity.ok(ApiResponse.success("Client trouvé", responseDTO));
    }


    @GetMapping("/telephone/{numeroTelephone}")
    public ResponseEntity<ApiResponse> getClientByTelephone(@PathVariable String numeroTelephone) {
        Client client = this.clientService.findByNumeroTelephone(numeroTelephone);
        ClientResponseDTO responseDTO =  this.clientMapper.toResponseDTO(client);

        return ResponseEntity.ok(ApiResponse.success("Client trouvé",  responseDTO));
    }


    @GetMapping("/count")
    public ResponseEntity<ApiResponse> countClients() {
        long count = this.clientService.countClients();
        String message = String.format("Nombre total de clients : %d", count);

        return ResponseEntity.ok(ApiResponse.success(message, count));
    }

    // ========================================================================
    // ==========      PROFIL DU CLIENT CONNECTÉ (sécurisé par token) ==========
    // ========================================================================

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMyProfile(Authentication authentication) {
        Client client = this.clientService.findByEmail(authentication.getName());
        ClientResponseDTO responseDTO = this.clientMapper.toResponseDTO(client);
        return ResponseEntity.ok(ApiResponse.success("Profil récupéré", responseDTO));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse> updateMyProfile(
            Authentication authentication, @Valid @RequestBody ClientUpdateDTO updateDTO) {
        Client client = this.clientService.findByEmail(authentication.getName());
        this.clientMapper.updateEntityFromDTO(client, updateDTO);
        Client clientMisAJour = this.clientService.updateClient(client.getIdClient(), client);
        ClientResponseDTO responseDTO = this.clientMapper.toResponseDTO(clientMisAJour);
        return ResponseEntity.ok(ApiResponse.success("Profil mis à jour avec succès", responseDTO));
    }

    @PutMapping("/me/password")
    public ResponseEntity<ApiResponse> changeMyPassword(
            Authentication authentication, @Valid @RequestBody ChangePasswordDTO dto) {
        this.clientService.changePassword(authentication.getName(), dto);
        return ResponseEntity.ok(ApiResponse.success("Mot de passe modifié avec succès", null));
    }

    @PostMapping("/me/photo")
    public ResponseEntity<ApiResponse> updateMyPhoto(
            Authentication authentication, @RequestParam("file") MultipartFile file) {
        String photoUrl = this.clientService.updatePhoto(authentication.getName(), file);
        return ResponseEntity.ok(ApiResponse.success("Photo mise à jour avec succès", photoUrl));
    }

    // type = cni-recto | cni-verso | passeport
    @PostMapping("/me/documents/{type}")
    public ResponseEntity<ApiResponse> uploadMyDocument(
            Authentication authentication, @PathVariable String type,
            @RequestParam("file") MultipartFile file) {
        this.clientService.uploadDocument(authentication.getName(), type, file);
        return ResponseEntity.ok(ApiResponse.success("Document envoyé avec succès", null));
    }

    @GetMapping("/me/documents/{type}")
    public ResponseEntity<Resource> getMyDocument(
            Authentication authentication, @PathVariable String type) {
        Resource resource = this.clientService.getDocument(authentication.getName(), type);
        return ResponseEntity.ok(resource);
    }

}
