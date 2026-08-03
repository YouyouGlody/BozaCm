package com.logondigital.bozacm.service.agence;

import com.logondigital.bozacm.DTO.AgenceRequestDTO;
import com.logondigital.bozacm.DTO.AgenceResponseDTO;
import com.logondigital.bozacm.DTO.PageResponseDTO;
import com.logondigital.bozacm.DTO.StatistiquesAgenceDetailDTO;
import com.logondigital.bozacm.entities.Agence;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.AgenceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional(readOnly = true)
public class AgenceServiceImpl implements AgenceService {

    private final AgenceRepository agenceRepository;

    public AgenceServiceImpl(AgenceRepository agenceRepository) {
        this.agenceRepository = agenceRepository;
    }

    // ─── Mapper central ───────────────────────────────────────────────────────

    private AgenceResponseDTO toDTO(Agence agence) {
        return new AgenceResponseDTO(
                agence.getId(), agence.getNom(), agence.getEmail(),
                agence.getTelephone(), agence.getAdresse()
        );
    }

    // ─── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public AgenceResponseDTO createAgence(AgenceRequestDTO dto) {
        // Contrôle anti-doublon sur le nom
        if (agenceRepository.existsByNomIgnoreCase(dto.getNom())) {
            throw new IllegalArgumentException(
                    "Une agence existe déjà avec le nom : " + dto.getNom());
        }
        // Contrôle anti-doublon sur l'email
        if (agenceRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new IllegalArgumentException(
                    "Une agence existe déjà avec l'email : " + dto.getEmail());
        }
        Agence agence = new Agence();
        agence.setNom(dto.getNom());
        agence.setEmail(dto.getEmail());
        agence.setTelephone(dto.getTelephone());
        agence.setAdresse(dto.getAdresse());
        return toDTO(agenceRepository.save(agence));
    }

    @Override
    public List<AgenceResponseDTO> getAllAgences() {
        return agenceRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public PageResponseDTO<AgenceResponseDTO> getAllAgencesPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<Agence> p = agenceRepository.findAll(pageable);
        return new PageResponseDTO<>(
                p.getContent().stream().map(this::toDTO).toList(),
                p.getNumber(), p.getSize(),
                p.getTotalElements(), p.getTotalPages(), p.isLast()
        );
    }

    @Override
    public AgenceResponseDTO getAgenceById(Integer id) {
        return agenceRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Agence introuvable avec l'id : " + id));
    }

    @Override
    @Transactional
    public AgenceResponseDTO updateAgence(Integer id, AgenceRequestDTO dto) {
        Agence agence = agenceRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Agence introuvable avec l'id : " + id));

        // Contrôle anti-doublon si le nom change
        if (!agence.getNom().equalsIgnoreCase(dto.getNom())
                && agenceRepository.existsByNomIgnoreCase(dto.getNom())) {
            throw new IllegalArgumentException(
                    "Une autre agence utilise déjà le nom : " + dto.getNom());
        }

        // Contrôle anti-doublon si l'email change
        if (!agence.getEmail().equalsIgnoreCase(dto.getEmail())
                && agenceRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new IllegalArgumentException(
                    "Une autre agence utilise déjà l'email : " + dto.getEmail());
        }

        agence.setNom(dto.getNom());
        agence.setAdresse(dto.getAdresse());
        agence.setEmail(dto.getEmail());
        agence.setTelephone(dto.getTelephone());
        return toDTO(agenceRepository.save(agence));
    }

    @Override
    @Transactional
    public void deleteAgence(Integer id) {
        if (!agenceRepository.existsById(id)) {
            throw new RessourceNotFoundException(
                    "Agence introuvable avec l'id : " + id);
        }
        agenceRepository.deleteById(id);
    }

    // ─── Recherches ───────────────────────────────────────────────────────────

    @Override
    public AgenceResponseDTO getAgenceByEmail(String email) {
        return agenceRepository.findByEmailIgnoreCase(email)
                .map(this::toDTO)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Agence introuvable avec l'email : " + email));
    }

    @Override
    public List<AgenceResponseDTO> getAgencesByVille(String ville) {
        return agenceRepository.findByVilleIgnoreCase(ville).stream()
                .map(this::toDTO).toList();
    }

    @Override
    public List<AgenceResponseDTO> rechercher(String terme) {
        return agenceRepository.rechercher(terme).stream()
                .map(a -> new AgenceResponseDTO(
                        a.getId(),
                        a.getNom(),
                        a.getEmail(),
                        a.getTelephone(),
                        a.getAdresse()
                ))
                .toList();
    }

    // ─── Fonctionnalités avancées — Statistiques ──────────────────────────────

    @Override
    public List<StatistiquesAgenceDetailDTO> getClassementAgences() {
        // 1 seule requête JPQL pour toutes les statistiques — pas de boucle N+1
        List<StatistiquesAgenceDetailDTO> classement =
                agenceRepository.findStatistiquesDetailleesToutesAgences();
        // Attribution des rangs en mémoire
        AtomicInteger rang = new AtomicInteger(1);
        classement.forEach(dto -> dto.setRang(rang.getAndIncrement()));
        return classement;
    }

    @Override
    public StatistiquesAgenceDetailDTO getStatistiquesAgence(Integer agenceId) {
        if (!agenceRepository.existsById(agenceId)) {
            throw new RessourceNotFoundException(
                    "Agence introuvable avec l'id : " + agenceId);
        }
        return agenceRepository.findStatistiquesParAgenceId(agenceId)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Statistiques introuvables pour l'agence : " + agenceId));
    }
}