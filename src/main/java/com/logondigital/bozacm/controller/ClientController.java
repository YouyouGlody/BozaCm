package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.service.client.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    // ========================================================================
    // ==========              CRUD DE BASE                          ==========
    // ========================================================================


    /**
     * Crée un nouveau client.

     * Endpoint : POST /api/v1/clients
     *
     * @param client les données du client à créer (JSON dans le body)
     * @return le client créé avec son ID généré (code 201 Created)
     * @PostMapping : Gère les requêtes HTTP POST
     * @Valid : Déclenche les validations (@NotNull, @Email, etc.) de l'entité
     * @RequestBody : Indique que les données viennent du corps de la requête (JSON)

     * Exemple de requête :
     * POST /api/v1/clients
     *           Body: {
     *                  "nom": "Dupont",
     *                  "prenom": "Jean",
     *                  "email": "jean@example.com",
     *                  "numeroTelephone": 612345678,
     *                  "adresse": "Yaoundé"
     *                  }

     * Réponse (201) :
     *          {
     *              "idClient": 1,
     *              "nom": "Dupont",
     *              "prenom": "Jean",
     *              "email": "jean@example.com",
     *              "numeroTelephone": 612345678,
     *              "adresse": "Yaoundé",
     *              "createdAt": "2025-10-12T14:30:00"
     *           }
     */
    @PostMapping(path = "/create")
    public ResponseEntity<Client> createClient(@Valid @RequestBody Client client) {
        // Appelle le service pour créer le client
        Client clientCree = this.clientService.createClient(client);
        // Retourne le client créé avec le code HTTP 201 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(clientCree);
    }


    /**
     * Récupère tous les clients.

     * Endpoint : GET /api/v1/clients

     * @return liste de tous les clients (code 200 OK)
     * @etMapping : Gère les requêtes HTTP GET

     * Exemple de requête :
     * GET /api/v1/clients

     * Réponse (200) :
     * [
     *   { "idClient": 1, "nom": "Dupont", ... },
     *   { "idClient": 2, "nom": "Martin", ... }
     * ]
     */
    @GetMapping(path ="/get_all" )
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }


    /**
     * Récupère un client par son ID.
     * Endpoint : GET /api/v1/clients/{id}

     * @param idClient l'ID du client à récupérer
     * @return le client trouvé (code 200 OK)
     * @throws RessourceNotFoundException si le client n'existe pas (géré par @ControllerAdvice).
     *
     * @PathVariable : Extrait la valeur de l'URL
     *                 Exemple : /api/v1/clients/5 → idClient = 5

     * Exemple de requête :
     * GET /api/v1/clients/1

     * Réponse (200) :
     * {
     *   "idClient": 1,
     *   "nom": "Dupont",
     *   "prenom": "Jean",
     *   ...
     * }
     */
    @GetMapping(path = "/get_by_id/{idClient}" )
    public ResponseEntity<Client> getClientById(@PathVariable Integer idClient) {
        Client client = clientService.getClientById(idClient);
        return ResponseEntity.ok(client);
    }



    /**
     * Met à jour un client existant.

     * Endpoint : PUT /api/v1/clients/{id}
     *
     * @param idClient l'ID du client à mettre à jour
     * @param client les nouvelles données du client
     * @return message de confirmation (code 200 OK)
     * @throws RessourceNotFoundException si le client n'existe pas
     *
     * @PutMapping : Gère les requêtes HTTP PUT (mise à jour complète)

     * Exemple de requête :
     * PUT /api/v1/clients/1
     * Body: {
     *   "nom": "Dupont",
     *   "prenom": "Jean",
     *   "email": "nouveau@example.com",
     *   "numeroTelephone": 698765432,
     *   "adresse": "Douala"
     * }

     * Réponse (200) :
     * "Client mis à jour avec succès"
     */
    @PutMapping(path = "/update/{idClient}")
    public ResponseEntity<String> updateClient(@PathVariable Integer idClient, @RequestBody Client client) {
        // Met à jour le client
        clientService.updateClient(idClient, client);

        // Retourne un message de confirmation
        return ResponseEntity.ok("Le Client avec ID " + idClient + " à été modifié avec succès ! ");
    }



    /**
     * Supprime un client par son ID.

     * Endpoint : DELETE /api/v1/clients/{id}
     *
     * @param idClient l'ID du client à supprimer
     * @return pas de contenu (code 204 No Content)
     * @throws RessourceNotFoundException si le client n'existe pas
     *
     * @DeleteMapping : Gère les requêtes HTTP DELETE

     * ResponseEntity.noContent().Build() : Retourne 204 (pas de body)
     *                                       Convention REST pour les suppressions

     * Exemple de requête :
     * DELETE /api/v1/clients/1
     *
     * Réponse (204) : Pas de contenu (succès)
     */
    @DeleteMapping("/{idClient}")
    public ResponseEntity<Void> deleteClient(@PathVariable Integer idClient) {
        // Supprime le client
        clientService.deleteClient(idClient);

        // Retourne 204 No Content (succès sans body)
        return ResponseEntity.noContent().build();
    }


    // ========================================================================
    // ==========              MÉTHODES MÉTIER                       ==========
    // ========================================================================


    /**
     * Recherche un client par son email.

     * Endpoint : GET /api/v1/clients/email/{email}
     *
     * @param email l'email du client à rechercher
     * @return le client trouvé (code 200 OK)
     * @throws RessourceNotFoundException si aucun client n'a cet email

     * Exemple de requête :
     * GET /api/v1/clients/email/jean@example.com

     * Réponse (200) :
     * {
     *   "idClient": 1,
     *   "nom": "Dupont",
     *   "email": "jean@example.com",
     *   ...
     * }
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Client> getClientByEmail(@PathVariable String email) {
        Client client = clientService.findByEmail(email);
        return ResponseEntity.ok(client);
    }

    /**
     * Recherche un client par son numéro de téléphone.

     * Endpoint : GET /api/v1/clients/telephone/{numeroTelephone}
     *
     * @param numeroTelephone le numéro de téléphone du client
     * @return le client trouvé (code 200 OK)
     * @throws RessourceNotFoundException si aucun client n'a ce numéro

     * Exemple de requête :
     * GET /api/v1/clients/telephone/612345678

     * Réponse (200) :
     * {
     *   "idClient": 1,
     *   "numeroTelephone": 612345678,
     *   ...
     * }
     */
    @GetMapping("/telephone/{numeroTelephone}")
    public ResponseEntity<Client> getClientByTelephone(@PathVariable String numeroTelephone) {
        Client client = clientService.findByNumeroTelephone(numeroTelephone);
        return ResponseEntity.ok(client);
    }

    /**
     * Compte le nombre total de clients.

     * Endpoint : GET /api/v1/clients/count
     *
     * @return le nombre total de clients (code 200 OK)

     * Exemple de requête :
     * GET /api/v1/clients/count

     * Réponse (200) : 42
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countClients() {
        long count = clientService.countClients();
        return ResponseEntity.ok(count);
    }

}
