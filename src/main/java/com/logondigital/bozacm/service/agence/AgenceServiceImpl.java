package com.logondigital.bozacm.service.agence;



import com.logondigital.bozacm.entities.Agence;
import com.logondigital.bozacm.exception.DatabaseException;
import com.logondigital.bozacm.exception.DuplicateResourceException;
import com.logondigital.bozacm.exception.InvalidRequestException;
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
        if (agence.getNom() == null || agence.getNom().isBlank()) {
            throw new InvalidRequestException("Le nom de l'agence est obligatoire !");
        }
        if (agence.getEmail() == null || !agence.getEmail().contains("@")) {
            throw new InvalidRequestException("L'email fourni n'est pas valide !");
        }

        // Vérifier doublon par email
        if (agenceRepo.findByEmail(agence.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Une agence avec cet email existe déjà !");
        }
        agence.setCreatedAt(new Date());
        try {
            this.agenceRepo.save(agence);

        }catch (Exception e){
            throw new DatabaseException("Erreur lors de la  creation de l'agence ");
        }

    }




    @Override
    public List<Agence> getAgences() {
        return this.agenceRepo.findAll();
    }

    @Override
    public Agence getAgenceById(Integer id) {
        return agenceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence avec ID " + id + " introuvable"));
    }

    @Override
    public String updateAgence(Integer agenceId, Agence agence) {
        Agence agenceToUpdate = this.agenceRepo.findById(agenceId).orElseThrow(
                () ->new ResourceNotFoundException("L'agence avec cette ID n'existe pas !")
        );
        agenceToUpdate.setName(agence.getName());
        agenceToUpdate.setUpdatedAt(new Date());
        try {
            this.agenceRepo.saveAndFlush(agenceToUpdate);
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors de la mise à jour de l'agence");
        }

        return "Agence updated with succes";
    }

    @Override
    public String deleteAgence(Integer agenceId) {
        try {
            this.agenceRepo.deleteById(agenceId);
            return "";
        }catch (Exception e){
            throw new DatabaseException("Erreur lors de la suppression de l'agence");
        }


    }

}
