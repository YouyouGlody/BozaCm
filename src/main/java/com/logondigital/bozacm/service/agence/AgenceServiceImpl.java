package com.logondigital.bozacm.service.agence;
import com.logondigital.bozacm.dto.AgenceRequestDTO;
import com.logondigital.bozacm.dto.AgenceResponseDTO;
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
    public void createAgence(AgenceRequestDTO dto) {
        Agence agence = new Agence();
        agence.setNom(dto.getNom());
        agence.setEmail(dto.getEmail());
        agence.setTelephone(dto.getTelephone());
        agence.setAdresse(dto.getAdresse());
        agence.setCreatedAt(new Date());
        agenceRepo.save(agence);
    }

    @Override
    public List<AgenceResponseDTO> getAllAgences() {
        return agenceRepo.findAll().stream()
                .map(a -> new AgenceResponseDTO(a.getId(), a.getNom(), a.getEmail(), a.getTelephone(), a.getAdresse()))
                .toList();
    }




    @Override
    public AgenceResponseDTO getAgenceById(Integer agenceId) {
        Agence agence = agenceRepo.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable"));
        return new AgenceResponseDTO(agence.getId(), agence.getNom(), agence.getEmail(), agence.getTelephone(), agence.getAdresse());

    }

    @Override
    public void updateAgence(Integer agenceId, AgenceRequestDTO dto) {
        Agence agenceToUpdate = agenceRepo.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("L'agence n'existe pas."));
        agenceToUpdate.setNom(dto.getNom());
        agenceToUpdate.setAdresse(dto.getAdresse());
        agenceToUpdate.setEmail(dto.getEmail());
        agenceToUpdate.setTelephone(dto.getTelephone());
        agenceToUpdate.setUpdatedAt(new Date());
        agenceRepo.saveAndFlush(agenceToUpdate);
    }

    @Override
    public void deleteAgence(Integer agenceId) {
        Agence agenceToDelete = agenceRepo.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("L'agence n'existe pas."));
        agenceRepo.deleteById(agenceId);
    }


    @Override
    public AgenceResponseDTO getAgenceByEmail(String email) {
        Agence agence = agenceRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable avec cet email"));

        return new AgenceResponseDTO(
                agence.getId(),
                agence.getNom(),
                agence.getEmail(),
                agence.getTelephone(),
                agence.getAdresse()
        );
    }

    @Override
    public List<AgenceResponseDTO> getAgencesByVille(String ville) {
        return agenceRepo.findByVille(ville).stream()
                .map(a -> new AgenceResponseDTO(
                        a.getId(),
                        a.getNom(),
                        a.getEmail(),
                        a.getTelephone(),
                        a.getAdresse()
                ))
                .toList();
    }
}