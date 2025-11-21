package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.dto.OffreResponseDTO;
import com.logondigital.bozacm.dto.PageResponseDTO;
import com.logondigital.bozacm.dto.ReservationRequestDTO;
import com.logondigital.bozacm.dto.ReservationResponseDTO;
import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.entities.ReservationOffre;
import com.logondigital.bozacm.exception.ResourceNotFoundException;
import com.logondigital.bozacm.repository.OffreRepo;
import com.logondigital.bozacm.repository.ReservationOffreRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReservationOffreServiceImpl implements ReservationOffreService {
    private final ReservationOffreRepo reservationOffreRepo;
    private final OffreRepo offreRepo;
    public ReservationOffreServiceImpl(ReservationOffreRepo reservationOffreRepo, OffreRepo offreRepo) {
        this.reservationOffreRepo = reservationOffreRepo;
        this.offreRepo = offreRepo;
    }

    @Override
    public void createReservation(ReservationRequestDTO dto) {
        Offre offre = offreRepo.findById(dto.getOffreId())
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));
        ReservationOffre reservationOffre = new ReservationOffre();
        reservationOffre.setNomClient(dto.getNomClient());
        reservationOffre.setEmailClient(dto.getEmailClient());
        reservationOffre.setDateReservation(dto.getDateReservation());
        reservationOffre.setStatut(dto.getStatut());
        reservationOffre.setOffre(offre);
        reservationOffre.setCreatedAt(new Date());

        reservationOffreRepo.save(reservationOffre);
    }

    @Override
    public List<ReservationResponseDTO> getAllReservations() {
        return reservationOffreRepo.findAll().stream()
                .map(r -> {
                    Offre offre = r.getOffre();

                    OffreResponseDTO offreDTO = new OffreResponseDTO(
                            offre.getId(),
                            offre.getTitre(),
                            offre.getDescription(),
                            offre.getPrix(),
                            offre.getDateDepart(),
                            offre.getAgence().getId(),
                            offre.getAgence().getNom(),
                            offre.getAgence().getEmail(),
                            offre.getAgence().getAdresse(),
                            offre.getAgence().getTelephone(),
                            offre.getTrajet().getId(),
                            offre.getTrajet().getDepart(),
                            offre.getTrajet().getArrivee(),
                            offre.getTrajet().getDuree()
                    );

                    return new ReservationResponseDTO(
                            r.getId(),
                            r.getNomClient(),
                            r.getEmailClient(),
                            r.getDateReservation(),
                            r.getStatut(),
                            offreDTO
                    );
                })
                .toList();
    }

    @Override
    public ReservationResponseDTO getReservationById(Integer reservationId) {
        ReservationOffre reservationOffre = reservationOffreRepo.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation introuvable"));

        Offre offre = reservationOffre.getOffre();

        OffreResponseDTO offreDTO = new OffreResponseDTO(
                offre.getId(),
                offre.getTitre(),
                offre.getDescription(),
                offre.getPrix(),
                offre.getDateDepart(),
                offre.getAgence().getId(),
                offre.getAgence().getNom(),
                offre.getAgence().getEmail(),
                offre.getAgence().getAdresse(),
                offre.getAgence().getTelephone(),
                offre.getTrajet().getId(),
                offre.getTrajet().getDepart(),
                offre.getTrajet().getArrivee(),
                offre.getTrajet().getDuree()
        );

        return new ReservationResponseDTO(
                reservationOffre.getId(),
                reservationOffre.getNomClient(),
                reservationOffre.getEmailClient(),
                reservationOffre.getDateReservation(),
                reservationOffre.getStatut(),
                offreDTO
        );
    }

    @Override
    public void updateReservation(Integer reservationId, ReservationRequestDTO dto) {
        ReservationOffre resToUpdate = reservationOffreRepo.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("La réservation n'existe pas."));
        resToUpdate.setNomClient(dto.getNomClient());
        resToUpdate.setEmailClient(dto.getEmailClient());
        resToUpdate.setDateReservation(dto.getDateReservation());
        resToUpdate.setStatut(dto.getStatut());
        resToUpdate.setUpdatedAt(new Date());
        reservationOffreRepo.saveAndFlush(resToUpdate);
    }

    @Override
    public void deleteReservation(Integer reservationId) {
        reservationOffreRepo.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("La réservation n'existe pas."));
        reservationOffreRepo.deleteById(reservationId);
    }


    @Override
    public PageResponseDTO<ReservationResponseDTO> getAllReservationsPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<ReservationOffre> reservationsPage = reservationOffreRepo.findAllWithOffreDetailsPaginated(pageable);

        List<ReservationResponseDTO> content = reservationsPage.getContent().stream()
                .map(r -> {
                    Offre offre = r.getOffre();
                    OffreResponseDTO offreDTO = new OffreResponseDTO(
                            offre.getId(),
                            offre.getTitre(),
                            offre.getDescription(),
                            offre.getPrix(),
                            offre.getDateDepart(),
                            offre.getAgence().getId(),
                            offre.getAgence().getNom(),
                            offre.getAgence().getEmail(),
                            offre.getAgence().getAdresse(),
                            offre.getAgence().getTelephone(),
                            offre.getTrajet().getId(),
                            offre.getTrajet().getDepart(),
                            offre.getTrajet().getArrivee(),
                            offre.getTrajet().getDuree()
                    );

                    return new ReservationResponseDTO(
                            r.getId(),
                            r.getNomClient(),
                            r.getEmailClient(),
                            r.getDateReservation(),
                            r.getStatut(),
                            offreDTO
                    );
                })
                .toList();

        return new PageResponseDTO<>(
                content,
                reservationsPage.getNumber(),
                reservationsPage.getSize(),
                reservationsPage.getTotalElements(),
                reservationsPage.getTotalPages(),
                reservationsPage.isLast()
        );
    }


    @Override
    public List<ReservationResponseDTO> getReservationsByStatut(String statut) {
        return reservationOffreRepo.findByStatut(statut).stream()
                .map(r -> {
                    Offre offre = r.getOffre();
                    OffreResponseDTO offreDTO = new OffreResponseDTO(
                            offre.getId(),
                            offre.getTitre(),
                            offre.getDescription(),
                            offre.getPrix(),
                            offre.getDateDepart(),
                            offre.getAgence().getId(),
                            offre.getAgence().getNom(),
                            offre.getAgence().getEmail(),
                            offre.getAgence().getAdresse(),
                            offre.getAgence().getTelephone(),
                            offre.getTrajet().getId(),
                            offre.getTrajet().getDepart(),
                            offre.getTrajet().getArrivee(),
                            offre.getTrajet().getDuree()
                    );

                    return new ReservationResponseDTO(
                            r.getId(),
                            r.getNomClient(),
                            r.getEmailClient(),
                            r.getDateReservation(),
                            r.getStatut(),
                            offreDTO
                    );
                })
                .toList();
    }

    @Override
    public List<ReservationResponseDTO> getReservationsByClient(String email) {
        return reservationOffreRepo.findByEmailClient(email).stream()
                .map(r -> {
                    Offre offre = r.getOffre();
                    OffreResponseDTO offreDTO = new OffreResponseDTO(
                            offre.getId(),
                            offre.getTitre(),
                            offre.getDescription(),
                            offre.getPrix(),
                            offre.getDateDepart(),
                            offre.getAgence().getId(),
                            offre.getAgence().getNom(),
                            offre.getAgence().getEmail(),
                            offre.getAgence().getAdresse(),
                            offre.getAgence().getTelephone(),
                            offre.getTrajet().getId(),
                            offre.getTrajet().getDepart(),
                            offre.getTrajet().getArrivee(),
                            offre.getTrajet().getDuree()
                    );

                    return new ReservationResponseDTO(
                            r.getId(),
                            r.getNomClient(),
                            r.getEmailClient(),
                            r.getDateReservation(),
                            r.getStatut(),
                            offreDTO
                    );
                })
                .toList();
    }

}
