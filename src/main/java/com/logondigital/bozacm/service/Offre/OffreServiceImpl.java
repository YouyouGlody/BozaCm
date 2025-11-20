package com.logondigital.bozacm.service.Offre;

import com.logondigital.bozacm.dto.OffreRequestDTO;
import com.logondigital.bozacm.dto.OffreResponseDTO;
import com.logondigital.bozacm.dto.PageResponseDTO;
import com.logondigital.bozacm.dto.RechercheOffreDTO;
import com.logondigital.bozacm.entities.Agence;
import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.entities.Trajet;
import com.logondigital.bozacm.exception.ResourceNotFoundException;
import com.logondigital.bozacm.repository.AgenceRepo;
import com.logondigital.bozacm.repository.OffreRepo;
import com.logondigital.bozacm.repository.TrajetRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OffreServiceImpl implements OffreService {
    private final OffreRepo offreRepo;
    private final AgenceRepo agenceRepo ;
    private final TrajetRepo trajetRepo;

    public OffreServiceImpl(OffreRepo offreRepo ,AgenceRepo agenceRepo, TrajetRepo trajetRepo) {
        this.offreRepo = offreRepo;
        this.agenceRepo = agenceRepo;
        this.trajetRepo = trajetRepo;
    }

    @Override
    public void createOffre(OffreRequestDTO dto) {

        Agence agence = agenceRepo.findById(dto.getAgenceId())

                .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable"));

        Trajet trajet = trajetRepo.findById(dto.getTrajetId())

                .orElseThrow(() -> new ResourceNotFoundException("Trajet introuvable"));

        Offre offre = new Offre();
        offre.setTitre(dto.getTitre());
        offre.setDescription(dto.getDescription());
        offre.setPrix(dto.getPrix());
        offre.setDateDepart(dto.getDateDepart());
        offre.setAgence(agence);
        offre.setTrajet(trajet);
        offre.setCreatedAt(new Date());

        offreRepo.save(offre);
    }

    @Override
    public List<OffreResponseDTO> getAllOffres() {
        List<Offre> offres = offreRepo.findAllWithAgenceAndTrajet();

        return offres.stream().map(offre -> {
            Agence agence = offre.getAgence();
            Trajet trajet = offre.getTrajet();

            return new OffreResponseDTO(
                    offre.getId(),
                    offre.getTitre(),
                    offre.getDescription(),
                    offre.getPrix(),
                    offre.getDateDepart(),

                    agence.getId(),
                    agence.getNom(),
                    agence.getEmail(),
                    agence.getAdresse(),
                    agence.getTelephone(),

                    trajet.getId(),
                    trajet.getDepart(),
                    trajet.getArrivee(),
                    trajet.getDuree()
            );
        }).toList();
    }

    @Override
    public OffreResponseDTO getOffreById(Integer offreId) {
        Offre offre = offreRepo.findById(offreId)
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));

        Agence agence = offre.getAgence();
        Trajet trajet = offre.getTrajet();

        return new OffreResponseDTO(
                offre.getId(),
                offre.getTitre(),
                offre.getDescription(),
                offre.getPrix(),
                offre.getDateDepart(),

                agence.getId(),
                agence.getNom(),
                agence.getEmail(),
                agence.getAdresse(),
                agence.getTelephone(),

                trajet.getId(),
                trajet.getDepart(),
                trajet.getArrivee(),
                trajet.getDuree()
        );
    }

    @Override
    public void updateOffre(Integer offreId, OffreRequestDTO dto) {
        Offre offreToUpdate = offreRepo.findById(offreId)
                .orElseThrow(() -> new ResourceNotFoundException("L'offre n'existe pas."));

        offreToUpdate.setTitre(dto.getTitre());
        offreToUpdate.setDescription(dto.getDescription());
        offreToUpdate.setPrix(dto.getPrix());
        offreToUpdate.setDateDepart(dto.getDateDepart());
        offreToUpdate.setUpdatedAt(new Date());
        offreRepo.saveAndFlush(offreToUpdate);
    }

    @Override
    public void deleteOffre(Integer offreId) {
        offreRepo.findById(offreId)
                .orElseThrow(() -> new ResourceNotFoundException("L'offre n'existe pas."));
        offreRepo.deleteById(offreId);
    }

    @Override
    public PageResponseDTO<OffreResponseDTO> getAllOffresPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<Offre> offrePage = offreRepo.findAll(pageable);

        List<OffreResponseDTO> content = offrePage.getContent().stream()
                .map(o -> new OffreResponseDTO(
                        o.getId(),
                        o.getTitre(),
                        o.getDescription(),
                        o.getPrix(),
                        o.getDateDepart(),
                        o.getAgence().getId(),
                        o.getAgence().getNom(),
                        o.getAgence().getEmail(),
                        o.getAgence().getAdresse(),
                        o.getAgence().getTelephone(),
                        o.getTrajet().getId(),
                        o.getTrajet().getDepart(),
                        o.getTrajet().getArrivee(),
                        o.getTrajet().getDuree()
                ))
                .toList();

        return new PageResponseDTO<>(
                content,
                offrePage.getNumber(),
                offrePage.getSize(),
                offrePage.getTotalElements(),
                offrePage.getTotalPages(),
                offrePage.isLast()
        );
    }


    @Override
    public List<OffreResponseDTO> rechercherOffres(RechercheOffreDTO criteres) {
        List<Offre> offres = offreRepo.rechercherOffres(
                criteres.getVilleDepart(),
                criteres.getVilleArrivee(),
                criteres.getPrixMin(),
                criteres.getPrixMax(),
                criteres.getDateDepart(),
                criteres.getAgenceId()
        );

        return offres.stream()
                .map(o -> new OffreResponseDTO(
                        o.getId(),
                        o.getTitre(),
                        o.getDescription(),
                        o.getPrix(),
                        o.getDateDepart(),
                        o.getAgence().getId(),
                        o.getAgence().getNom(),
                        o.getAgence().getEmail(),
                        o.getAgence().getAdresse(),
                        o.getAgence().getTelephone(),
                        o.getTrajet().getId(),
                        o.getTrajet().getDepart(),
                        o.getTrajet().getArrivee(),
                        o.getTrajet().getDuree()
                ))
                .toList();
    }
    @Override
    public List<OffreResponseDTO> getOffresByPrixRange(Double prixMin, Double prixMax) {
        return offreRepo.findByPrixBetween(prixMin, prixMax).stream()
                .map(o -> new OffreResponseDTO(
                        o.getId(),
                        o.getTitre(),
                        o.getDescription(),
                        o.getPrix(),
                        o.getDateDepart(),
                        o.getAgence().getId(),
                        o.getAgence().getNom(),
                        o.getAgence().getEmail(),
                        o.getAgence().getAdresse(),
                        o.getAgence().getTelephone(),
                        o.getTrajet().getId(),
                        o.getTrajet().getDepart(),
                        o.getTrajet().getArrivee(),
                        o.getTrajet().getDuree()
                ))
                .toList();
    }

    @Override
    public List<OffreResponseDTO> getOffresByAgence(Integer agenceId) {
        return offreRepo.findByAgenceId(agenceId).stream()
                .map(o -> new OffreResponseDTO(
                        o.getId(),
                        o.getTitre(),
                        o.getDescription(),
                        o.getPrix(),
                        o.getDateDepart(),
                        o.getAgence().getId(),
                        o.getAgence().getNom(),
                        o.getAgence().getEmail(),
                        o.getAgence().getAdresse(),
                        o.getAgence().getTelephone(),
                        o.getTrajet().getId(),
                        o.getTrajet().getDepart(),
                        o.getTrajet().getArrivee(),
                        o.getTrajet().getDuree()
                ))
                .toList();
    }
}