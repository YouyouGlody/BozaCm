package com.logondigital.bozacm.service.agence;
import com.logondigital.bozacm.dto.AgenceRequestDTO;
import com.logondigital.bozacm.dto.AgenceResponseDTO;
import com.logondigital.bozacm.dto.StatistiquesAgenceDetailDTO;
import com.logondigital.bozacm.entities.Agence;
import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.exception.CustomResourceNotFoundException;
import com.logondigital.bozacm.repository.AgenceRepo;
import com.logondigital.bozacm.repository.OffreRepo;
import com.logondigital.bozacm.repository.ReservationOffreRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AgenceServiceImpl implements AgenceService {
    private final AgenceRepo agenceRepo;
    private final OffreRepo offreRepo;
    private final ReservationOffreRepo reservationOffreRepo;


    public AgenceServiceImpl(AgenceRepo agenceRepo, OffreRepo offreRepo, ReservationOffreRepo reservationOffreRepo) {
        this.agenceRepo = agenceRepo;
        this.offreRepo = offreRepo;
        this.reservationOffreRepo = reservationOffreRepo;
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
                .orElseThrow(() -> new CustomResourceNotFoundException("Agence introuvable"));
        return new AgenceResponseDTO(agence.getId(), agence.getNom(), agence.getEmail(), agence.getTelephone(), agence.getAdresse());

    }



    @Override
    public void updateAgence(Integer agenceId, AgenceRequestDTO dto) {
        Agence agenceToUpdate = agenceRepo.findById(agenceId)
                .orElseThrow(() -> new CustomResourceNotFoundException("L'agence n'existe pas."));
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
                .orElseThrow(() -> new CustomResourceNotFoundException("L'agence n'existe pas."));
        agenceRepo.deleteById(agenceId);
    }


    @Override
    public AgenceResponseDTO getAgenceByEmail(String email) {
        Agence agence = agenceRepo.findByEmail(email)
                .orElseThrow(() -> new CustomResourceNotFoundException("Agence introuvable avec cet email"));

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
    @Override
    public List<StatistiquesAgenceDetailDTO> getClassementAgences() {
        List<Object[]> statsOffres = offreRepo.getStatistiquesParAgence();

        List<StatistiquesAgenceDetailDTO> classement = new ArrayList<>();
        int rang = 1;

        for (Object[] stat : statsOffres) {
            Integer agenceId = (Integer) stat[0];
            String agenceNom = (String) stat[1];
            Long nombreOffres = (Long) stat[2];
            Double prixMoyen = (Double) stat[3];
            Double prixMin = (Double) stat[4];
            Double prixMax = (Double) stat[5];

            // Récupérer l'agence complète
            Agence agence = agenceRepo.findById(agenceId).orElse(null);
            if (agence == null) continue;

            // Récupérer les statistiques de réservations
            Long nbReservationsTotal = reservationOffreRepo.countReservationsByAgenceId(agenceId);
            if (nbReservationsTotal == null) nbReservationsTotal = 0L;

            Long nbReservationsConfirmees = reservationOffreRepo.countReservationsByAgenceIdAndStatut(agenceId, "confirmée");
            if (nbReservationsConfirmees == null) nbReservationsConfirmees = 0L;

            // Calculer le taux de confirmation
            Double tauxConfirmation = 0.0;
            if (nbReservationsTotal > 0) {
                tauxConfirmation = (nbReservationsConfirmees.doubleValue() / nbReservationsTotal.doubleValue()) * 100;
                tauxConfirmation = Math.round(tauxConfirmation * 100.0) / 100.0; // Arrondir à 2 décimales
            }

            // Récupérer le chiffre d'affaires
            Double chiffreAffaire = reservationOffreRepo.getChiffreAffaireByAgenceId(agenceId, "confirmée");
            if (chiffreAffaire == null) chiffreAffaire = 0.0;

            StatistiquesAgenceDetailDTO dto = new StatistiquesAgenceDetailDTO(
                    agenceId,
                    agenceNom,
                    agence.getEmail(),
                    agence.getTelephone(),
                    agence.getAdresse(),
                    nombreOffres,
                    prixMoyen,
                    prixMin,
                    prixMax,
                    nbReservationsTotal,
                    nbReservationsConfirmees,
                    tauxConfirmation,
                    chiffreAffaire,
                    rang
            );

            classement.add(dto);
            rang++;
        }

        return classement;
    }

    @Override
    public StatistiquesAgenceDetailDTO getStatistiquesAgence(Integer agenceId) {
        Agence agence = agenceRepo.findById(agenceId)
                .orElseThrow(() -> new CustomResourceNotFoundException("Agence introuvable"));

        Long nombreOffres = offreRepo.countOffresByAgenceId(agenceId);
        if (nombreOffres == null) nombreOffres = 0L;

        // Calculer prix moyen, min, max
        List<Offre> offres = offreRepo.findByAgenceId(agenceId);
        Double prixMoyen = 0.0;
        Double prixMin = 0.0;
        Double prixMax = 0.0;

        if (!offres.isEmpty()) {
            prixMoyen = offres.stream().mapToDouble(Offre::getPrix).average().orElse(0.0);
            prixMoyen = Math.round(prixMoyen * 100.0) / 100.0;

            prixMin = offres.stream().mapToDouble(Offre::getPrix).min().orElse(0.0);
            prixMax = offres.stream().mapToDouble(Offre::getPrix).max().orElse(0.0);
        }

        // Statistiques réservations
        Long nbReservationsTotal = reservationOffreRepo.countReservationsByAgenceId(agenceId);
        if (nbReservationsTotal == null) nbReservationsTotal = 0L;

        Long nbReservationsConfirmees = reservationOffreRepo.countReservationsByAgenceIdAndStatut(agenceId, "confirmée");
        if (nbReservationsConfirmees == null) nbReservationsConfirmees = 0L;

        Double tauxConfirmation = 0.0;
        if (nbReservationsTotal > 0) {
            tauxConfirmation = (nbReservationsConfirmees.doubleValue() / nbReservationsTotal.doubleValue()) * 100;
            tauxConfirmation = Math.round(tauxConfirmation * 100.0) / 100.0;
        }

        Double chiffreAffaire = reservationOffreRepo.getChiffreAffaireByAgenceId(agenceId, "confirmée");
        if (chiffreAffaire == null) chiffreAffaire = 0.0;

        return new StatistiquesAgenceDetailDTO(
                agenceId,
                agence.getNom(),
                agence.getEmail(),
                agence.getTelephone(),
                agence.getAdresse(),
                nombreOffres,
                prixMoyen,
                prixMin,
                prixMax,
                nbReservationsTotal,
                nbReservationsConfirmees,
                tauxConfirmation,
                chiffreAffaire,
                null  // Pas de rang pour une agence seule
        );
    }
}