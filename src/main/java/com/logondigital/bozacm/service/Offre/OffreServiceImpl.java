package com.logondigital.bozacm.service.Offre;

import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.exception.DatabaseException;
import com.logondigital.bozacm.exception.InvalidRequestException;
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
    public String createOffre(Offre offre) {
        if (offre.getTitre() == null || offre.getTitre().isBlank()) {
            throw new InvalidRequestException("Le titre de l'offre est obligatoire !");
        }
        if (offre.getPrix() == null || offre.getPrix() <= 0) {
            throw new InvalidRequestException("Le prix doit être supérieur à 0 !");
        }

        // Vérifier que l’agence existe
        if (offre.getAgence() == null || offre.getAgence().getId() == null) {
            throw new InvalidRequestException("Une offre doit être liée à une agence !");
        }

        // Vérifier que le trajet existe
        if (offre.getTrajet() == null || offre.getTrajet().getId() == null) {
            throw new InvalidRequestException("Une offre doit être liée à un trajet !");
        }

        offre.setCreatedAt(new Date());
       this.offreRepo.save(offre);
        return "Offre créée avec succès !";
    }

    @Override
    public List<Offre> getOffres() {
        return this.offreRepo.findAll();
    }

    @Override
    public Offre getOffreById(Integer offreId) {
        return this.offreRepo.findById(offreId) .orElseThrow(() -> new ResourceNotFoundException("L’offre introuvable essayer un autre id."));
    }

    @Override
    public String updateOffre(Integer offreId, Offre offre) {
       try {

           Offre offreToUpdate = this.offreRepo.findById(offreId).orElseThrow(() -> new ResourceNotFoundException("L’offre avec cette id n’existe pas."));

           if (offre.getPrix() <= 0) {
               throw new InvalidRequestException("Le prix doit être supérieur à 0.");
           }
           offreToUpdate.setName(offre.getName());
           offreToUpdate.setUpdatedAt(new Date());
           this.offreRepo.saveAndFlush(offreToUpdate);

           return "Offre updated with succes";
       }catch (Exception e) {
           throw new DatabaseException("Erreur lors mise a jour de l'offre: " + e.getMessage());
       }
    }

    @Override
    public String deleteOffre(Integer offreId) {
        Offre offreToDelete = this.offreRepo.findById(offreId).orElseThrow(() -> new ResourceNotFoundException("L’offre cette n’existe pas essaye un autre."));
        this.offreRepo.deleteById(offreId);
        return "";
    }

    @Override
    public void CreateOffre(Offre offre) {

    }
}
