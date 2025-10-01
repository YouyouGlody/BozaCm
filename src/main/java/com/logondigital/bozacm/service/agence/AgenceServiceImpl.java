package com.logondigital.bozacm.service.agence;



import com.logondigital.bozacm.entities.Agence;
import com.logondigital.bozacm.repository.AgenceRepo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AgenceServiceImpl implements AgenceService {
    private final AgenceRepo agenceRepo;

    public AgenceServiceImpl(AgenceRepo agenceRepo) {
        this.agenceRepo = agenceRepo;
    }

    @Override
    public String createAgence(Agence agence) {
        agence.setCreatedAt(new Date());
        this.agenceRepo.save(agence);
        return "Agence created";
    }




    @Override
    public List<Agence> getAgences() {
        return this.agenceRepo.findAll();
    }

    @Override
    public Agence getAgenceById(Integer agenceId) {
        return this.agenceRepo.findById(agenceId).get();
    }

    @Override
    public String updateAgence(Integer agenceId, Agence agence) {
        Agence agenceToUpdate = this.agenceRepo.findById(agenceId).get();
        agenceToUpdate.setName(agence.getName());
        agenceToUpdate.setUpdatedAt(new Date());
        this.agenceRepo.saveAndFlush(agenceToUpdate);

        return "Agence updated with succes";
    }

    @Override
    public String deleteAgence(Integer agenceId) {
        this.agenceRepo.deleteById(agenceId);
        return "";
    }

    @Override
    public void CreateAgence(Agence agence) {

    }
}
