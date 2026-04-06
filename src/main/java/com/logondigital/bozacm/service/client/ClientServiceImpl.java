package com.logondigital.bozacm.service.client;

import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.exceptions.EmailAlreadyExistsException;
import com.logondigital.bozacm.exceptions.PhoneAlreadyExistsException;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.ClientRepo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implémentation du service Client.
 * Contient toute la logique métier pour la gestion des clients.
 */

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepo clientRepo;



    // Injection de dépendance via le constructeur
    /**
     * Injection de dépendance via le constructeur.
     * Recommandé car :
     * - Immutabilité (final)
     * - Testabilité (facile de mocker)
     * - Pas besoin de @Autowired (Spring le fait automatiquement)
     */
    public ClientServiceImpl(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }


    // ===========================================================
    // ==========        CRUD DE BASE DE CLIENT       =====
    // ===========================================================



    // 1. Créer un nouveau client
    /**
     * Crée un nouveau client après validation.
     */

    @Override
    public Client createClient(Client client) {
        // Validation 1 : Vérifier si l'email existe déjà
        if (clientRepo.existsByEmail(client.getEmail())) {
            throw new EmailAlreadyExistsException(client.getEmail());
        }

        // Validation 2 : Vérifier si le numéro de téléphone existe déjà
        if (clientRepo.existsByNumeroTelephone(client.getNumeroTelephone())) {
            throw new PhoneAlreadyExistsException(client.getNumeroTelephone());
        }

        // PAS de setCreatedAt() ici : @PrePersist s'en charge!

        // Sauvegarder dans la BD et retourner le client créé
        return this.clientRepo.save(client);
    }


    // 2. Récupérer un client par son ID
    /**
     * Récupère un client par son ID.
     */
    @Override
    public Client getClientById(Integer idClient) {
        return this.clientRepo.findById(idClient).
                orElseThrow(
                        () -> new RessourceNotFoundException("Client non trouvé avec l'ID: " + idClient)
                );

    }


    // 3. Récupérer tous les clients
    /**
     * Récupère tous les clients.
     */
    @Override
    public List<Client> getAllClients() {
        // Retourne la liste de tous les clients
       return clientRepo.findAll();
    }


    // 4. Mettre à jour les informations d’un client
    /**
     * Met à jour les informations d'un client.
     *
     * @return
     */
    @Override
    public Client updateClient(Integer idClient, Client client) {

        // Étape 1 : Récupérer le client existant
        Client clientToUpdate = this.clientRepo.findById(idClient). orElseThrow(
                () -> new RessourceNotFoundException("Client non trouvé avec l'ID: " + idClient)
        );


        // Étape 2 : Vérifier si l'email change et est unique

        // Vérification d'email null safe
        String currentEmail = clientToUpdate.getEmail();
        String newEmail = client.getEmail();

        // Vérifier si le nouvel email est différent de l'actuel et existe déjà
        boolean emailChanged = (currentEmail == null && newEmail != null) ||
                (currentEmail != null && !currentEmail.equals(newEmail));

        if (emailChanged && newEmail != null && clientRepo.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException(newEmail);
        }

        // Étape 3 : Vérifier si le numéro de téléphone change et est unique
        String currentPhone = clientToUpdate.getNumeroTelephone();
        String newPhone = client.getNumeroTelephone();

        boolean phoneChanged = (currentPhone == null && newPhone != null) ||
                (currentPhone != null && !currentPhone.equals(newPhone));

        if (phoneChanged && newPhone != null && clientRepo.existsByNumeroTelephone(newPhone)) {
            throw new PhoneAlreadyExistsException(newPhone);
        }

        // Étape 4 : Mettre à jour les champs
        clientToUpdate.setNom(client.getNom());
        clientToUpdate.setPrenom(client.getPrenom());
        clientToUpdate.setEmail(newEmail);
        clientToUpdate.setNumeroTelephone(newPhone);
        clientToUpdate.setAdresse(client.getAdresse());

        // PAS de setUpdatedAt() ici : @PreUpdate s'en charge !

        // Étape 5 : Sauvegarder et retourner
        this.clientRepo.save(clientToUpdate);


        return clientToUpdate;
    }

    /**
     * Supprime un client par son ID.
     */
    @Override
    public void deleteClient(Integer idClient) {

        // Vérifier que le client existe avant de supprimer
        if (!clientRepo.existsById(idClient)) {
            throw new RessourceNotFoundException("Client non trouvé avec l'ID: " + idClient);
        }

        // Supprime le client par ID
        clientRepo.deleteById(idClient);
    }


    // ========== Méthodes Métier ==========


    /**
     * Trouve un client par son email.
     */
    @Override
    public Client findByEmail(String email) {
        return clientRepo.findByEmail(email).orElseThrow(
                () -> new RessourceNotFoundException("Client non trouvé avec l'email: " + email)
        );
    }


    /**
     * Trouve un client par son numéro de téléphone.
     */
    @Override
    public Client findByNumeroTelephone(String numeroTelephone) {
        return clientRepo.findByNumeroTelephone(numeroTelephone)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Client non trouvé avec le numéro: " + numeroTelephone
                ));
    }



    /**
     * Compte le nombre total de clients.
     */
    @Override
    public long countClients() {
        return clientRepo.count();
    }
}
