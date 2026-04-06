package com.logondigital.bozacm.service.client;

import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.exceptions.EmailAlreadyExistsException;
import com.logondigital.bozacm.exceptions.PhoneAlreadyExistsException;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;

import java.util.List;

/**
 *Un Service contient la logique métier de ton application.
 * C'est la couche entre le Controller (qui reçoit les requêtes) et le Repository (qui accède à la BD).

 * Service pour gérer la logique métier des clients.
 * Contient les opérations CRUD et les validations métier.
 */

public interface ClientService {

    // ===========================================================
    // ==========        CRUD DE BASE DE CLIENT       =====
    // ===========================================================




    // 1. Créer un nouveau client
    /**
     * Crée un nouveau client après validation.
     * Vérifie que l'email et le numéro de téléphone sont uniques.
     *
     * @param client Le client à créer
     * @throws EmailAlreadyExistsException Si l'email existe déjà
     * @throws PhoneAlreadyExistsException Si le numéro existe déjà
     */
    Client createClient(Client client);



    // 2. Récupérer un client par son ID
    /**
     * Récupère un client par son ID.
     *
     * @param idClient L'ID du client
     * @return Le client trouvé
     * @throws RessourceNotFoundException Si le client n'existe pas
     */
    Client getClientById(Integer idClient);



    // 3. Récupérer tous les clients
    /**
     * Récupère tous les clients.
     *
     * @return Liste de tous les clients
     */
    List<Client> getAllClients();



    // 4. Mettre à jour les informations d’un client
    /**
     * Met à jour les informations d'un client.
     * Vérifie que le nouvel email (s'il change) est unique.
     *
     * @param idClient L'ID du client à mettre à jour
     * @param client   Les nouvelles informations
     * @return
     * @throws RessourceNotFoundException  Si le client n'existe pas
     * @throws EmailAlreadyExistsException Si le nouvel email existe déjà
     */
    Client updateClient(Integer idClient, Client client);



    // 5. Supprimer un client par son ID
    /**
     * Supprime un client par son ID.
     *
     * @param idClient L'ID du client à supprimer
     * @throws RessourceNotFoundException Si le client n'existe pas
     */
    void deleteClient(Integer idClient);


    // ========== Méthodes Métier ==========


    /**
     * Trouve un client par son email.
     * Utilisé pour l'authentification.
     *
     * @param email L'adresse email
     * @return Le client trouvé
     * @throws RessourceNotFoundException Si le client n'existe pas
     */
    Client findByEmail(String email);



    /**
     * Trouve un client par son numéro de téléphone.
     *
     * @param numeroTelephone Le numéro de téléphone
     * @return Le client trouvé
     * @throws RessourceNotFoundException Si le client n'existe pas
     */
    Client findByNumeroTelephone(String numeroTelephone);


    /**
     * Compte le nombre total de clients.
     *
     * @return Le nombre de clients
     */
    long countClients();


}
