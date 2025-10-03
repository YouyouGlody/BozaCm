package com.logondigital.bozacm.service.billet;

import com.logondigital.bozacm.entities.Billet;

import java.util.List;

public interface BilletService {

    // CRUD de base

    void createBillet(Billet billet);

    List<Billet> getAllBillets();

    Billet getBilletById(Integer idBillet);


    void deleteBilletById(Integer idBillet);

    void deleteAllBillets();  // Supprimer tous les billets




    // Méthodes supplémentaires

    long countBillets();  // Compter le nombre de billets


}
