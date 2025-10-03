package com.logondigital.bozacm.service.client;

import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.repository.ClientRepo;

import java.util.Date;
import java.util.List;

public class ClientServiceImpl implements ClientService {

    private final ClientRepo clientRepo;

    // Injection de dépendance via le constructeur
    public ClientServiceImpl(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    // 1. Créer un nouveau client
    @Override
    public void createClient(Client client) {
        client.setCreatedAt(new Date()); // Définir la date de création
        this.clientRepo.save(client);
    }


    // 2. Récupérer un client par son ID
    @Override
    public Client getClientById(Integer idClient) {
        return this.clientRepo.findById(idClient).get();
    }


    // 3. Récupérer tous les clients
    @Override
    public List<Client> getAllClients() {
        // Retourne la liste de tous les clients
        return clientRepo.findAll();
    }


    // 4. Mettre à jour les informations d’un client
    @Override
    public void updateClient(Integer idClient, Client client) {
        // Vérifier si le client existe
        Client existingClient = clientRepo.findById(idClient).orElse(null);

        if (existingClient != null) {
            // Met à jour uniquement les champs nécessaires
            existingClient.setNom(client.getNom());
            existingClient.setPrenom(client.getPrenom());
            existingClient.setEmail(client.getEmail());
            existingClient.setNumeroTelephone(client.getNumeroTelephone());

            // Sauvegarde des modifications
            clientRepo.save(existingClient);
        }
        // Sinon : rien à faire pour l’instant (plus tard, je pourrais gérer une exception).

    }

    @Override
    public void deleteClient(Integer idClient) {
        // Supprime le client par ID
        clientRepo.deleteById(idClient);
    }
}
