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

    @Override
    public void createBillet(Billet billet) {
        billet.getCreatedAt(new Date()); // Modification du set en get
        this.billetRepo.save(billet);

    }

    @Override
    public List<Billet> getAllBillets() {
        return billetRepo.findAll();
    }

    @Override
    public Billet getBilletById(Integer idBillet) {
        return this.billetRepo.findById(idBillet).orElseThrow(
                () -> new RessourceNotFoundException("La catégorie avec l'id " + idBillet + " n'existe pas !")
        );;
    }

    @Override
    public void updateBillet(Billet billet) {

    }

    @Override
    public void deleteBilletById(Integer idBillet) {

    }

    @Override
    public Billet getBilletByNumeroBillet(String numeroBillet) {
        return null;
    }
}
