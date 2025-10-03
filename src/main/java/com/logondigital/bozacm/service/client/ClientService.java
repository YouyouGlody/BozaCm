package com.logondigital.bozacm.service.client;

import com.logondigital.bozacm.entities.Client;

import java.util.List;

public interface ClientService {

    // 1. Créer un nouveau client
    void createClient(Client client);

    // 2. Récupérer un client par son ID
    Client getClientById(Integer idClient);

    // 3. Récupérer tous les clients
    List<Client> getAllClients();

    // 4. Mettre à jour les informations d’un client
    void updateClient(Integer idClient, Client client);

    // 5. Supprimer un client par son ID
    void deleteClient(Integer idClient);


}
