package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.service.client.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    //    - 'injection de dépendances par constructeur pour `ClientService`
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // ✅ Créer un client
    @PostMapping(path = "/create")
    public ResponseEntity<String> createClient(@Valid @RequestBody Client client) {
        //Créer le client
        this.clientService.createClient(client);
        //Retourne le méssage
        return ResponseEntity.status(201).body("Le Client à été créé avec succès !");
    }


    // ✅ Récupérer tous les clients
    @GetMapping(path ="/get_all" )
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.status(200).body(this.clientService.getAllClients());
    }

    // ✅ Récupérer un client par ID
    @GetMapping(path = "/get_by_id/{idClient}" )
    public ResponseEntity<Client> getClientById(@PathVariable Integer idClient) {
        return ResponseEntity.status(200).body(this.clientService.getClientById(idClient));
    }

    // ✅ Mettre à jour un client
    @PutMapping(path = "/update/{idClient}")
    public ResponseEntity<String> updateClient(@PathVariable Integer idClient, @RequestBody Client client) {
        //Modifie la catégorie
        clientService.updateClient(idClient, client);
        //Retourne le méssage
        return ResponseEntity.status(202)
                .body("Le Client avec ID " + idClient + " à été modifié avec succès ! ");
    }

    // ✅ Supprimer un client
    @DeleteMapping(path = "/delete/{idClient}")
    public ResponseEntity<String> deleteClient(@PathVariable Integer idClient) {
        // Récupérer la catégorie avant suppression pour le message
        Client client = clientService.getClientById(idClient);
        // Supprimer la catégorie
        this.clientService.deleteClient(idClient);
        // Retourner le message
        return ResponseEntity.status(202)
                .body("Le Client avec l'ID " + idClient + " à été supprimé avec succès !");
    }

}
