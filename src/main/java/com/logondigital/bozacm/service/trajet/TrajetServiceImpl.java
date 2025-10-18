package com.logondigital.bozacm.service.trajet;


import com.logondigital.bozacm.entities.Trajet;
import com.logondigital.bozacm.exception.DatabaseException;
import com.logondigital.bozacm.exception.InvalidRequestException;
import com.logondigital.bozacm.exception.ResourceNotFoundException;
import com.logondigital.bozacm.repository.TrajetRepo;
import jakarta.persistence.Id;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service

public class TrajetServiceImpl implements TrajetService {
    private final TrajetRepo trajetRepo;

    public TrajetServiceImpl(TrajetRepo trajetRepo) {
        this.trajetRepo = trajetRepo;
    }

    @Override
    public void createTrajet(Trajet trajet) {
        if (trajet.getDepart() == null || trajet.getArrivee() == null) {
            throw new InvalidRequestException("Le départ et l’arrivée sont obligatoires !");
        }
        trajet.setCreatedAt(new Date());
        this.trajetRepo.save(trajet);

    }

    @Override
    public List<Trajet> getTrajets() {
        return this.trajetRepo.findAll();
    }

    @Override
    public Trajet getTrajetById(Integer trajetId) {
        return this.trajetRepo.findById(trajetId) .orElseThrow(() -> new ResourceNotFoundException("Le trajet avec cette ID  n’existe pas."));

    }

    @Override
    public String updateTrajet(Integer trajetId, Trajet trajet) {
        try {
            Trajet trajetToUpdate = this.trajetRepo.findById(trajetId).orElseThrow(() -> new ResourceNotFoundException("Le trajet avec l’ID  n’existe pas."));
            trajetToUpdate.setName(trajet.getName());
            trajetToUpdate.setUpdatedAt(new Date());
            this.trajetRepo.saveAndFlush(trajetToUpdate);

            return "Trajet updated with succes";
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors de la mise à jour du trajet : " + e.getMessage());
        }
    }

    @Override
    public String deleteTrajet(Integer trajetId) {
        Trajet trajetToDelete = this.trajetRepo.findById(trajetId)
                .orElseThrow(() -> new ResourceNotFoundException("Le trajet  n’existe pas."));
        this.trajetRepo.deleteById(trajetId);
        return "";
    }


}

