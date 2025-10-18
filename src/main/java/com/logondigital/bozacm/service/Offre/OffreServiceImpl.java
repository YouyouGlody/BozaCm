package com.logondigital.bozacm.service.Offre;

import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.exception.ResourceNotFoundException;
import com.logondigital.bozacm.repository.OffreRepo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OffreServiceImpl implements OffreService {
    private final OffreRepo offreRepo;

    public OffreServiceImpl(OffreRepo offreRepo) {
        this.offreRepo = offreRepo;
    }

    @Override
    public void createOffre(Offre offre) {
        offre.setCreatedAt(new Date());
        offreRepo.save(offre);
    }

    @Override
    public List<Offre> getOffres() {
        return offreRepo.findAll();
    }

    @Override
    public Offre getOffreById(Integer offreId) {
        return offreRepo.findById(offreId)
                .orElseThrow(() -> new ResourceNotFoundException("L'offre n'existe pas."));
    }

    @Override
    public void updateOffre(Integer offreId, Offre offre) {
        Offre offreToUpdate = offreRepo.findById(offreId)
                .orElseThrow(() -> new ResourceNotFoundException("L'offre n'existe pas."));
        // Mettre à jour les champs autorisés
        offreToUpdate.setTitre(offre.getTitre());
        offreToUpdate.setDescription(offre.getDescription());
        offreToUpdate.setPrix(offre.getPrix());
        offreToUpdate.setDateDepart(offre.getDateDepart());
        offreToUpdate.setAgence(offre.getAgence());
        offreToUpdate.setTrajet(offre.getTrajet());
        offreToUpdate.setUpdatedAt(new Date());
        offreRepo.saveAndFlush(offreToUpdate);
    }

    @Override
    public void deleteOffre(Integer offreId) {
        offreRepo.findById(offreId)
                .orElseThrow(() -> new ResourceNotFoundException("L'offre n'existe pas."));
        offreRepo.deleteById(offreId);
    }
}