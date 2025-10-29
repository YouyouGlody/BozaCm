package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.dto.OffreResponseDTO;
import com.logondigital.bozacm.dto.PageResponseDTO;
import com.logondigital.bozacm.dto.ReservationRequestDTO;
import com.logondigital.bozacm.dto.ReservationResponseDTO;
import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.entities.Reservation;
import com.logondigital.bozacm.exception.ResourceNotFoundException;
import com.logondigital.bozacm.repository.OffreRepo;
import com.logondigital.bozacm.repository.ReservationRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepo reservationRepo;
    private final OffreRepo offreRepo;
    public ReservationServiceImpl(ReservationRepo reservationRepo, OffreRepo offreRepo) {
        this.reservationRepo = reservationRepo;
        this.offreRepo = offreRepo;
    }

    @Override
    public void createReservation(ReservationRequestDTO dto) {
        Offre offre = offreRepo.findById(dto.getOffreId())
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));

        Reservation reservation = new Reservation();
        reservation.setNomClient(dto.getNomClient());
        reservation.setEmailClient(dto.getEmailClient());
        reservation.setDateReservation(dto.getDateReservation());
        reservation.setStatut(dto.getStatut());
        reservation.setOffre(offre);
        reservation.setCreatedAt(new Date());

        reservationRepo.save(reservation);
    }

    @Override
    public List<ReservationResponseDTO> getAllReservations() {
        return reservationRepo.findAll().stream()
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
        Reservation reservation = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation introuvable"));

        Offre offre = reservation.getOffre();

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
                reservation.getId(),
                reservation.getNomClient(),
                reservation.getEmailClient(),
                reservation.getDateReservation(),
                reservation.getStatut(),
                offreDTO
        );
    }

    @Override
    public void updateReservation(Integer reservationId, ReservationRequestDTO dto) {
        Reservation resToUpdate = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("La réservation n'existe pas."));
        resToUpdate.setNomClient(dto.getNomClient());
        resToUpdate.setEmailClient(dto.getEmailClient());
        resToUpdate.setDateReservation(dto.getDateReservation());
        resToUpdate.setStatut(dto.getStatut());
        resToUpdate.setUpdatedAt(new Date());
        reservationRepo.saveAndFlush(resToUpdate);
    }

    @Override
    public void deleteReservation(Integer reservationId) {
        reservationRepo.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("La réservation n'existe pas."));
        reservationRepo.deleteById(reservationId);
    }


    @Override
    public PageResponseDTO<ReservationResponseDTO> getAllReservationsPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<Reservation> reservationsPage = reservationRepo.findAllWithOffreDetailsPaginated(pageable);

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

}