package com.logondigital.bozacm.service.agence;

import com.logondigital.bozacm.entities.Agence;
import com.logondigital.bozacm.exception.ResourceNotFoundException;
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
    public void createAgence(Agence agence) {
        agence.setCreatedAt(new Date());
        agenceRepo.save(agence);
    }

    @Override
    public List<Agence> getAgences() {
        return agenceRepo.findAll();
    }

    @Override
    public Agence getAgenceById(Integer agenceId) {
        return agenceRepo.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("L'agence n'existe pas."));
    }

    @Override
    public void updateAgence(Integer agenceId, Agence agence) {
        Agence agenceToUpdate = agenceRepo.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("L'agence n'existe pas."));
        agenceToUpdate.setNom(agence.getNom());
        agenceToUpdate.setAdresse(agence.getAdresse());
        agenceToUpdate.setEmail(agence.getEmail());
        agenceToUpdate.setTelephone(agence.getTelephone());
        agenceToUpdate.setUpdatedAt(new Date());
        agenceRepo.saveAndFlush(agenceToUpdate);
    }

    @Override
    public void deleteAgence(Integer agenceId) {
        Agence agenceToDelete = agenceRepo.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("L'agence n'existe pas."));
        agenceRepo.deleteById(agenceId);
    }
}