package com.logondigital.bozacm.service.trajet;

import com.logondigital.bozacm.dto.TrajetRequestDTO;
import com.logondigital.bozacm.dto.TrajetResponseDTO;
import com.logondigital.bozacm.entities.Trajet;
import com.logondigital.bozacm.exception.CustomResourceNotFoundException;
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
    public void createTrajet(TrajetRequestDTO dto) {
        Trajet trajet = new Trajet();
        trajet.setDepart(dto.getVilleDepart());
        trajet.setArrivee(dto.getVilleArrivee());
        trajet.setDuree(dto.getDuree());
        trajet.setCreatedAt(new Date());
        trajetRepo.save(trajet);
    }

    @Override
    public List<TrajetResponseDTO> getAllTrajets() {
        return trajetRepo.findAll().stream()
                .map(t -> new TrajetResponseDTO(t.getId(), t.getDepart(), t.getArrivee(), t.getDuree()))
                .toList();
    }

    @Override
    public TrajetResponseDTO getTrajetById(Integer id) {
        Trajet trajet = trajetRepo.findById(id)
                .orElseThrow(() -> new CustomResourceNotFoundException("Trajet introuvable"));
        return new TrajetResponseDTO(trajet.getId(), trajet.getDepart(), trajet.getArrivee(), trajet.getDuree());
    }

    @Override
    public void updateTrajet(Integer trajetId, TrajetRequestDTO dto) {
        Trajet trajetToUpdate = trajetRepo.findById(trajetId)
                .orElseThrow(() -> new CustomResourceNotFoundException("Le trajet n'existe pas."));
        trajetToUpdate.setDepart(dto.getVilleDepart());
        trajetToUpdate.setArrivee(dto.getVilleArrivee());
        trajetToUpdate.setDuree(dto.getDuree());
        trajetToUpdate.setUpdatedAt(new Date());
        trajetRepo.saveAndFlush(trajetToUpdate);
    }

    @Override
    public void deleteTrajet(Integer trajetId) {
        trajetRepo.findById(trajetId)
                .orElseThrow(() -> new CustomResourceNotFoundException("Le trajet n'existe pas."));
        trajetRepo.deleteById(trajetId);
    }

    @Override
    public List<TrajetResponseDTO> getTrajetsByDepart(String depart) {
        return trajetRepo.findByDepart(depart).stream()
                .map(t -> new TrajetResponseDTO(
                        t.getId(),
                        t.getDepart(),
                        t.getArrivee(),
                        t.getDuree()
                ))
                .toList();
    }

    @Override
    public List<TrajetResponseDTO> getTrajetsByRoute(String depart, String arrivee) {
        return trajetRepo.findByDepartAndArrivee(depart, arrivee).stream()
                .map(t -> new TrajetResponseDTO(
                        t.getId(),
                        t.getDepart(),
                        t.getArrivee(),
                        t.getDuree()
                ))
                .toList();
    }
}