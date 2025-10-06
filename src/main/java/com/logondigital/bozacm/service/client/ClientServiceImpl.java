package com.logondigital.bozacm.service.client;

import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.exceptions.EmailAlreadyExistsException;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.ClientRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        // Vérifier si l'email existe déjà
        if (clientRepo.existsByEmail(client.getEmail())) {
            throw new EmailAlreadyExistsException(client.getEmail());
        }

        client.setCreatedAt(LocalDateTime.now()); // Définir la date de création
        this.clientRepo.save(client);
    }


    // 2. Récupérer un client par son ID
    @Override
    public Client getClientById(Integer idClient) {
        return this.clientRepo.findById(idClient).
                orElseThrow(
                        () -> new RessourceNotFoundException("Client non trouvé avec l'ID: " + idClient)
                );

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
        Client clientToUpdate = this.clientRepo.findById(idClient). orElseThrow(
                () -> new RessourceNotFoundException("Client non trouvé avec l'ID: " + idClient)
        );

        // Vérification d'email null-safe
        String currentEmail = clientToUpdate.getEmail();
        String newEmail = client.getEmail();

        // Vérifier si le nouvel email est différent de l'actuel et existe déjà
        boolean emailChanged = (currentEmail == null && newEmail != null) ||
                (currentEmail != null && !currentEmail.equals(newEmail));

        if (emailChanged && newEmail != null && clientRepo.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException(newEmail);
        }

        // Mettre à jour les champs
        clientToUpdate.setNom(client.getNom());
        clientToUpdate.setPrenom(client.getPrenom());
        clientToUpdate.setEmail(newEmail);
        clientToUpdate.setNumeroTelephone(client.getNumeroTelephone());
        clientToUpdate.setAdresse(client.getAdresse());
        clientToUpdate.setUpdatedAt(LocalDateTime.now());

        this.clientRepo.save(clientToUpdate);


    }

    @Override
    public void deleteClient(Integer idClient) {
        // Supprime le client par ID
        clientRepo.deleteById(idClient);
    }
}
