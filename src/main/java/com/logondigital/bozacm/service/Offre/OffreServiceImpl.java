package com.logondigital.bozacm.service.Offre;

import com.logondigital.bozacm.entities.Offre;
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
        offre.setCreatedAt(new Date());
        this.offreRepo.save(offre);
        return "Offre created";
    }

    @Override
    public List<Offre> getOffres() {
        return this.offreRepo.findAll();
    }

    @Override
    public Offre getOffreById(Integer offreId) {
        return this.offreRepo.findById(offreId).get();
    }

    @Override
    public String updateOffre(Integer offreId, Offre offre) {
        Offre offreToUpdate = this.offreRepo.findById(offreId).get();
        offreToUpdate.setName(offre.getName());
        offreToUpdate.setUpdatedAt(new Date());
        this.offreRepo.saveAndFlush(offreToUpdate);

        return "Offre updated with succes";
    }

    @Override
    public String deleteOffre(Integer offreId) {
        this.offreRepo.deleteById(offreId);
        return "";
    }

    @Override
    public void CreateOffre(Offre offre) {

    }
}
