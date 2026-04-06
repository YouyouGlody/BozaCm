package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Un Repository est une interface qui permet de communiquer avec la base de données sans écrire de SQL.
  Repository pour gérer les opérations CRUD sur l'entité Client.
 *   Hérite de JpaRepository qui fournit des méthodes de base :
 *   – save() : Créer ou mettre à jour un client
 *   – findById() : Trouver un client par son ID
 *   – findAll() : Récupérer tous les clients
 *   – delete() : Supprimer un client
 *   – count() : Compter le nombre de clients
 */
public interface ClientRepo extends JpaRepository<Client, Integer> {

    /**
     * Trouve un client par son adresse email.
     * Utile pour :
     *  - La connexion (authentification).
     *  - Vérifier si un email existe déjà lors de l'inscription

     * @param email L'adresse email à rechercher
     * @return Optional<Client> contenant le client s'il existe, sinon vide

     * SQL généré automatiquement :
     * SELECT * FROM clients WHERE email = ?
     */
    Optional<Client> findByEmail(String email);

    /**
     * Vérifie si un client existe avec cette adresse email.
     * Plus rapide que findByEmail() car ne charge pas toute l'entité.
     * Utile pour la validation lors de l'inscription.

     * @param email L'adresse email à vérifier
     * @return true si l'email existe déjà, false sinon

     * SQL généré automatiquement :
     * SELECT COUNT(*) > 0 FROM clients WHERE email = ?
     */
    boolean existsByEmail(String email);

    /**
     * Trouve un client par son numéro de téléphone.
     * Utile pour la recherche ou la récupération de compte.

     * @param numeroTelephone Le numéro de téléphone à rechercher
     * @return Optional<Client> contenant le client s'il existe, sinon vide

     * SQL généré automatiquement :
     * SELECT * FROM clients WHERE numero_telephone = ?
     */
    Optional<Client> findByNumeroTelephone(String numeroTelephone);

    /**
     * Vérifie si un client existe avec ce numéro de téléphone.

     * @param numeroTelephone Le numéro de téléphone à vérifier
     * @return true si le numéro existe déjà, false sinon
     */
    boolean existsByNumeroTelephone(String numeroTelephone);


}
