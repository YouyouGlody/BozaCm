package com.logondigital.bozacm.service.trajet;


import com.logondigital.bozacm.entities.Trajet;
import com.logondigital.bozacm.repository.TrajetRepo;

import java.util.Date;
import java.util.List;

public class TrajetServiceImpl implements TrajetService {
    private final TrajetRepo trajetRepo;

    public TrajetServiceImpl(TrajetRepo trajetRepo) {
        this.trajetRepo = trajetRepo;
    }

    @Override
    public String createTrajet(Trajet trajet) {
        trajet.setCreatedAt(new Date());
        this.trajetRepo.save(trajet);
        return "Trajet created";
    }

    @Override
    public List<Trajet> getTrajets() {
        return this.trajetRepo.findAll();
    }

    @Override
    public Trajet getTrajetById(Integer trajetId) {
        return this.trajetRepo.findById(trajetId).get();
    }

    @Override
    public String updateTrajet(Integer trajetId, Trajet trajet) {
        Trajet trajetToUpdate = this.trajetRepo.findById(trajetId).get();
        trajetToUpdate.setName(trajet.getName());
        trajetToUpdate.setUpdatedAt(new Date());
        this.trajetRepo.saveAndFlush(trajetToUpdate);

        return "Trajet updated with succes";
    }

    @Override
    public String deleteTrajet(Integer trajetId) {
        this.trajetRepo.deleteById(trajetId);
        return "";
    }

    @Override
    public void CreateTrajet(Trajet trajet) {

    }
}

