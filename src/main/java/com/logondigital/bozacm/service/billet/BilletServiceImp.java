package com.logondigital.bozacm.service.billet;

import com.logondigital.bozacm.entities.Billet;
import com.logondigital.bozacm.repository.BilletRepo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BilletServiceImp implements BilletService {

    private final BilletRepo billetRepo;

    public BilletServiceImp(BilletRepo billetRepo) {
        this.billetRepo = billetRepo;
    }

    // Créer un billet
    @Override
    public void createBillet(Billet billet) {
        billet.setCreatedAt(new Date()); // Définir la date de création
        this.billetRepo.save(billet);

    }

    // Récupérer tous les billets
    @Override
    public List<Billet> getAllBillets() {
        return billetRepo.findAll();
    }

    // Récupérer un billet par ID
    @Override
    public Billet getBilletById(Integer idBillet) {
        return this.billetRepo.findById(idBillet).get();
    }


    //Supprimer un billet par ID
    @Override
    public void deleteBilletById(Integer idBillet) {
        this.billetRepo.deleteById(idBillet);
    }


    // Supprimer tous les billets
    @Override
    public void deleteAllBillets() {
        billetRepo.deleteAll();
    }



    // Compter le nombre total de billets
    @Override
    public long countBillets() {
        return billetRepo.count();
    }
}
