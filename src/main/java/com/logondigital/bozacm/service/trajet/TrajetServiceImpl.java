package com.logondigital.bozacm.service.trajet;

import com.logondigital.bozacm.DTO.PageResponseDTO;
import com.logondigital.bozacm.DTO.TrajetRequestDTO;
import com.logondigital.bozacm.DTO.TrajetResponseDTO;
import com.logondigital.bozacm.entities.Trajet;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
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
public class TrajetServiceImpl implements TrajetService {

    private final TrajetRepository trajetRepository;

    public TrajetServiceImpl(TrajetRepository trajetRepository) {
        this.trajetRepository = trajetRepository;
    }

    // ─── Mapper central ────────────────────────────────────────────────────────

    private TrajetResponseDTO toDTO(Trajet trajet) {
        return new TrajetResponseDTO(
                trajet.getId(),
                trajet.getDepart(),
                trajet.getArrivee(),
                trajet.getDuree()
        );
    }

    // ─── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public TrajetResponseDTO createTrajet(TrajetRequestDTO dto) {
        // Vérification doublon départ + arrivée (insensible à la casse)
        if (trajetRepository.existsByDepartAndArriveeIgnoreCase(dto.getVilleDepart(), dto.getVilleArrivee())) {
            throw new IllegalArgumentException(
                    "Un trajet existe déjà pour la route : "
                            + dto.getVilleDepart() + " → " + dto.getVilleArrivee()
            );
        }

        Trajet trajet = new Trajet();
        trajet.setDepart(dto.getVilleDepart());
        trajet.setArrivee(dto.getVilleArrivee());
        trajet.setDuree(dto.getDuree());
        // createdAt géré automatiquement par @CreatedDate

        return toDTO(trajetRepository.save(trajet));
    }

    @Override
    public List<TrajetResponseDTO> getAllTrajets() {
        return trajetRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public PageResponseDTO<TrajetResponseDTO> getAllTrajetsPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<Trajet> trajetPage = trajetRepository.findAll(pageable);

        List<TrajetResponseDTO> content = trajetPage.getContent().stream()
                .map(this::toDTO)
                .toList();

        return new PageResponseDTO<>(
                content,
                trajetPage.getNumber(),
                trajetPage.getSize(),
                trajetPage.getTotalElements(),
                trajetPage.getTotalPages(),
                trajetPage.isLast()
        );
    }

    @Override
    public TrajetResponseDTO getTrajetById(Integer id) {
        return trajetRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RessourceNotFoundException("Trajet introuvable avec l'id : " + id));
    }

    @Override
    @Transactional
    public TrajetResponseDTO updateTrajet(Integer id, TrajetRequestDTO dto) {
        Trajet trajet = trajetRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Trajet introuvable avec l'id : " + id));

        // Vérification doublon si la route change
        boolean routeChange = !trajet.getDepart().equalsIgnoreCase(dto.getVilleDepart())
                || !trajet.getArrivee().equalsIgnoreCase(dto.getVilleArrivee());

        if (routeChange && trajetRepository.existsByDepartAndArriveeIgnoreCase(
                dto.getVilleDepart(), dto.getVilleArrivee())) {
            throw new IllegalArgumentException(
                    "Un trajet existe déjà pour la route : "
                            + dto.getVilleDepart() + " → " + dto.getVilleArrivee()
            );
        }

        trajet.setDepart(dto.getVilleDepart());
        trajet.setArrivee(dto.getVilleArrivee());
        trajet.setDuree(dto.getDuree());
        // updatedAt géré automatiquement par @LastModifiedDate

        return toDTO(trajetRepository.save(trajet));
    }

    @Override
    @Transactional
    public void deleteTrajet(Integer id) {
        // Vérifier que le trajet n'a pas d'offres actives avant suppression
        Long nbOffres = trajetRepository.countOffresByTrajetId(id);
        if (nbOffres != null && nbOffres > 0) {
            throw new IllegalStateException(
                    "Impossible de supprimer ce trajet : il est lié à " + nbOffres + " offre(s) active(s)."
            );
        }
        if (!trajetRepository.existsById(id)) {
            throw new RessourceNotFoundException("Trajet introuvable avec l'id : " + id);
        }
        trajetRepository.deleteById(id);
    }

    // ─── Recherches ───────────────────────────────────────────────────────────

    @Override
    public List<TrajetResponseDTO> getTrajetsByDepart(String depart) {
        // Utilise LIKE insensible à la casse via JPQL
        return trajetRepository.findByDepartContainingIgnoreCase(depart, Pageable.unpaged())
                .getContent().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<TrajetResponseDTO> getTrajetsByRoute(String depart, String arrivee) {
        return trajetRepository.findByDepartAndArriveeIgnoreCase(depart, arrivee)
                .map(t -> List.of(toDTO(t)))
                .orElse(List.of());
    }
    @Override
    public List<TrajetResponseDTO> rechercher(String terme) {
        return trajetRepository.rechercher(terme).stream()
                .map(this::toDTO)
                .toList();
    }
}