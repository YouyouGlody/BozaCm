package com.logondigital.bozacm.service.client;

import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.repository.ClientRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepo clientRepo;

    // Injection de dépendance via le constructeur
    public ClientServiceImpl(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    // 1. Créer un nouveau client
    @Override
    public void createClient(Client client) {
        client.setCreatedAt(LocalDateTime.now()); // Définir la date de création
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
        Client clientToUpdate = this.clientRepo.findById(idClient).get();
        clientToUpdate.setNom(client.getNom());
        clientToUpdate.setPrenom(client.getPrenom());
        clientToUpdate.setNumeroTelephone(client.getNumeroTelephone());
        clientToUpdate.setEmail(client.getEmail());
        clientToUpdate.setAdresse(client.getAdresse());
        clientToUpdate.setUpdatedAt(LocalDateTime.now());
        this.clientRepo.saveAndFlush(clientToUpdate);
    }

    @Override
    public void deleteClient(Integer idClient) {
        // Supprime le client par ID
        clientRepo.deleteById(idClient);
    }
}
