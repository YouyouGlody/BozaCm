package com.logondigital.bozacm.service.billet;

import com.logondigital.bozacm.entities.Billet;
import com.logondigital.bozacm.enums.StatutBillet;

import java.util.List;

/**
 *Un Service contient la logique métier de ton application.
 * C'est la couche entre le Controller (qui reçoit les requêtes) et le Repository (qui accède à la BD).
*/

public interface BilletService {

    // ===========================================================
    // ==========        CRUD DE BASE DE BILLET       =====
    // ===========================================================

    void createBillet(Billet billet);

    List<Billet> getAllBillets();

    Billet getBilletById(Integer idBillet);


    void deleteBilletById(Integer idBillet);

    void deleteAllBillets();  // Supprimer tous les billets

    long countBillets();  // Compter le nombre de billets



    // ===========================================================
    // ==========        MÉTHODES MÉTIER       =====
    // ===========================================================


    /**
     * Trouve un billet par son numéro unique (validation QR code).
     */
    Billet findByNumeroBillet(String numeroBillet);

    /**
     * Récupère tous les billets d'un client.
     */
    List<Billet> getBilletsClient(Integer clientId);

    /**
     * Récupère les billets d'un client par statut.
     */
    List<Billet> getBilletsParStatut(Integer clientId, StatutBillet statut);

    /**
     * Trouve un billet par l'ID de sa réservation.
     */
    Billet getBilletByReservation(Integer reservationId);

    /**
     * Compte le nombre de billets d'un client.
     */
    long countBilletsByClient(Integer clientId);

    /**
     * Marque un billet comme utilisé.
     */
    Billet marquerBilletUtilise(String numeroBillet);

    /**
     * Expire automatiquement les billets périmés.
     */
    void expirerBilletsPerimes();


}
