package com.logondigital.bozacm.service.Offre;

import com.logondigital.bozacm.DTO.AgenceResponseDTO;
import com.logondigital.bozacm.DTO.OffreRequestDTO;
import com.logondigital.bozacm.DTO.OffreResponseDTO;
import com.logondigital.bozacm.DTO.PageResponseDTO;
import com.logondigital.bozacm.DTO.RechercheOffreDTO;
import com.logondigital.bozacm.DTO.TrajetResponseDTO;
import com.logondigital.bozacm.entities.Agence;
import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.entities.Trajet;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.AgenceRepository;
import com.logondigital.bozacm.repository.OffreRepository;
import com.logondigital.bozacm.repository.TrajetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OffreServiceImpl implements OffreService {

    private final OffreRepository  offreRepository;
    private final AgenceRepository agenceRepository;
    private final TrajetRepository trajetRepository;

    public OffreServiceImpl(OffreRepository offreRepository,
                            AgenceRepository agenceRepository,
                            TrajetRepository trajetRepository) {
        this.offreRepository  = offreRepository;
        this.agenceRepository = agenceRepository;
        this.trajetRepository = trajetRepository;
    }

    // ─── Mapper central ───────────────────────────────────────────────────────

    private OffreResponseDTO toDTO(Offre offre) {
        Agence agence = offre.getAgence();
        Trajet trajet = offre.getTrajet();
        return new OffreResponseDTO(
                offre.getId(), offre.getTitre(), offre.getDescription(),
                offre.getPrix(), offre.getDateDepart(),
                offre.getNombrePlaces(), offre.getPlacesDisponibles(),
                new AgenceResponseDTO(agence.getId(), agence.getNom(),
                        agence.getEmail(), agence.getTelephone(), agence.getAdresse()),
                new TrajetResponseDTO(trajet.getId(), trajet.getDepart(),
                        trajet.getArrivee(), trajet.getDuree())
        );
    }

    private PageResponseDTO<OffreResponseDTO> toPageDTO(Page<Offre> page) {
        return new PageResponseDTO<>(
                page.getContent().stream().map(this::toDTO).toList(),
                page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast()
        );
    }

    // ─── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public OffreResponseDTO createOffre(OffreRequestDTO dto) {
        Agence agence = agenceRepository.findById(dto.getAgenceId())
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Agence introuvable avec l'id : " + dto.getAgenceId()));
        Trajet trajet = trajetRepository.findById(dto.getTrajetId())
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Trajet introuvable avec l'id : " + dto.getTrajetId()));

        Offre offre = new Offre();
        offre.setTitre(dto.getTitre());
        offre.setDescription(dto.getDescription());
        offre.setPrix(dto.getPrix());
        offre.setDateDepart(dto.getDateDepart());
        offre.setNombrePlaces(dto.getNombrePlaces());
        // placesDisponibles initialisé automatiquement via @PrePersist
        offre.setAgence(agence);
        offre.setTrajet(trajet);

        return toDTO(offreRepository.save(offre));
    }

    @Override
    public List<OffreResponseDTO> getAllOffres() {
        return offreRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public PageResponseDTO<OffreResponseDTO> getAllOffresPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return toPageDTO(offreRepository.findAll(pageable));
    }

    @Override
    public OffreResponseDTO getOffreById(Integer id) {
        return offreRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Offre introuvable avec l'id : " + id));
    }

    @Override
    @Transactional
    public OffreResponseDTO updateOffre(Integer id, OffreRequestDTO dto) {
        Offre offre = offreRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Offre introuvable avec l'id : " + id));

        if (!offre.getAgence().getId().equals(dto.getAgenceId())) {
            Agence agence = agenceRepository.findById(dto.getAgenceId())
                    .orElseThrow(() -> new RessourceNotFoundException(
                            "Agence introuvable avec l'id : " + dto.getAgenceId()));
            offre.setAgence(agence);
        }
        if (!offre.getTrajet().getId().equals(dto.getTrajetId())) {
            Trajet trajet = trajetRepository.findById(dto.getTrajetId())
                    .orElseThrow(() -> new RessourceNotFoundException(
                            "Trajet introuvable avec l'id : " + dto.getTrajetId()));
            offre.setTrajet(trajet);
        }

        offre.setTitre(dto.getTitre());
        offre.setDescription(dto.getDescription());
        offre.setPrix(dto.getPrix());
        offre.setDateDepart(dto.getDateDepart());
        offre.setNombrePlaces(dto.getNombrePlaces());

        return toDTO(offreRepository.save(offre));
    }

    @Override
    @Transactional
    public void deleteOffre(Integer id) {
        if (!offreRepository.existsById(id)) {
            throw new RessourceNotFoundException("Offre introuvable avec l'id : " + id);
        }
        offreRepository.deleteById(id);
    }

    // ─── Recherches ───────────────────────────────────────────────────────────

    @Override
    public PageResponseDTO<OffreResponseDTO> rechercherOffres(
            RechercheOffreDTO criteres, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateDepart").ascending());
        return toPageDTO(offreRepository.rechercherOffres(
                criteres.getVilleDepart(), criteres.getVilleArrivee(),
                criteres.getPrixMin(), criteres.getPrixMax(),
                criteres.getDateDepart(), criteres.getAgenceId(), pageable));
    }

    @Override
    public List<OffreResponseDTO> getOffresByPrixRange(Double prixMin, Double prixMax) {
        if (prixMin > prixMax) {
            throw new IllegalArgumentException(
                    "prixMin ne peut pas être supérieur à prixMax");
        }
        return offreRepository.findByPrixBetween(prixMin, prixMax)
                .stream().map(this::toDTO).toList();
    }

    @Override
    public PageResponseDTO<OffreResponseDTO> getOffresByAgence(
            Integer agenceId, int page, int size) {
        if (!agenceRepository.existsById(agenceId)) {
            throw new RessourceNotFoundException(
                    "Agence introuvable avec l'id : " + agenceId);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateDepart").ascending());
        return toPageDTO(offreRepository.findByAgenceId(agenceId, pageable));
    }
}