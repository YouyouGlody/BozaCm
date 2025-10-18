package com.logondigital.bozacm.service.trajet;

import com.logondigital.bozacm.entities.Trajet;
import com.logondigital.bozacm.exception.ResourceNotFoundException;
import com.logondigital.bozacm.repository.TrajetRepo;
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
        trajet.setCreatedAt(new Date());
        trajetRepo.save(trajet);
    }

    @Override
    public List<Trajet> getTrajets() {
        return trajetRepo.findAll();
    }

    @Override
    public Trajet getTrajetById(Integer trajetId) {
        return trajetRepo.findById(trajetId)
                .orElseThrow(() -> new ResourceNotFoundException("Le trajet n'existe pas."));
    }

    @Override
    public void updateTrajet(Integer trajetId, Trajet trajet) {
        Trajet trajetToUpdate = trajetRepo.findById(trajetId)
                .orElseThrow(() -> new ResourceNotFoundException("Le trajet n'existe pas."));
        trajetToUpdate.setDepart(trajet.getDepart());
        trajetToUpdate.setArrivee(trajet.getArrivee());
        trajetToUpdate.setDuree(trajet.getDuree());
        trajetToUpdate.setUpdatedAt(new Date());
        trajetRepo.saveAndFlush(trajetToUpdate);
    }

    @Override
    public void deleteTrajet(Integer trajetId) {
        trajetRepo.findById(trajetId)
                .orElseThrow(() -> new ResourceNotFoundException("Le trajet n'existe pas."));
        trajetRepo.deleteById(trajetId);
    }
}