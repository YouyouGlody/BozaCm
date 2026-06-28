package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.DTO.ReservationRequestDTO;
import com.logondigital.bozacm.DTO.AgenceResponseDTO;
import com.logondigital.bozacm.DTO.OffreResponseDTO;
import com.logondigital.bozacm.DTO.PageResponseDTO;
import com.logondigital.bozacm.DTO.ReservationResponseDTO;
import com.logondigital.bozacm.DTO.TrajetResponseDTO;
import com.logondigital.bozacm.entities.Agence;
import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.entities.ReservationOffre;
import com.logondigital.bozacm.entities.ReservationOffre.StatutReservation;
import com.logondigital.bozacm.entities.Trajet;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.OffreRepository;
import com.logondigital.bozacm.repository.ReservationOffreRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationOffreServiceImpl implements ReservationOffreService {

    private final ReservationOffreRepository reservationOffreRepository;
    private final OffreRepository            offreRepository;

    public ReservationOffreServiceImpl(ReservationOffreRepository reservationOffreRepository,
                                       OffreRepository offreRepository) {
        this.reservationOffreRepository = reservationOffreRepository;
        this.offreRepository            = offreRepository;
    }

    // ─── Mappers centraux ─────────────────────────────────────────────────────

    private OffreResponseDTO offreToDTO(Offre offre) {
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

    private ReservationResponseDTO toDTO(ReservationOffre r) {
        return new ReservationResponseDTO(
                r.getId(), r.getNomClient(), r.getEmailClient(),
                r.getDateReservation(), r.getStatut(), offreToDTO(r.getOffre())
        );
    }

    private PageResponseDTO<ReservationResponseDTO> toPageDTO(Page<ReservationOffre> page) {
        return new PageResponseDTO<>(
                page.getContent().stream().map(this::toDTO).toList(),
                page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast()
        );
    }

    // ─── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public ReservationResponseDTO createReservation(@Valid ReservationRequestDTO dto) {
        Offre offre = offreRepository.findById(dto.getOffreId())
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Offre introuvable avec l'id : " + dto.getOffreId()));

        // Contrôle anti-doublon : un client ne peut pas réserver 2x la même offre
        if (reservationOffreRepository.existsByEmailClientAndOffreId(
                dto.getEmailClient(), dto.getOffreId())) {
            throw new IllegalArgumentException(
                    "Le client " + dto.getEmailClient() + " a déjà réservé cette offre.");
        }

        // Vérification des places disponibles (fonctionnalité avancée)
        if (offre.getPlacesDisponibles() <= 0) {
            throw new IllegalStateException(
                    "L'offre \"" + offre.getTitre() + "\" est complète. Plus de places disponibles.");
        }

        ReservationOffre reservation = new ReservationOffre();
        reservation.setNomClient(dto.getNomClient());
        reservation.setEmailClient(dto.getEmailClient());
        reservation.setDateReservation(dto.getDateReservation());
        reservation.setStatut(dto.getStatut());
        reservation.setOffre(offre);

        ReservationOffre saved = reservationOffreRepository.save(reservation);

        // Décrémenter les places disponibles après réservation confirmée
        offre.setPlacesDisponibles(offre.getPlacesDisponibles() - 1);
        offreRepository.save(offre);

        return toDTO(saved);
    }

    @Override
    public List<ReservationResponseDTO> getAllReservations() {
        return reservationOffreRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public PageResponseDTO<ReservationResponseDTO> getAllReservationsPaginated(
            int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return toPageDTO(reservationOffreRepository.findAll(pageable));
    }

    @Override
    public ReservationResponseDTO getReservationById(Integer id) {
        return reservationOffreRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Réservation introuvable avec l'id : " + id));
    }

    @Override
    @Transactional
    public ReservationResponseDTO updateReservation(Integer id, ReservationRequestDTO dto) {
        ReservationOffre reservation = reservationOffreRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Réservation introuvable avec l'id : " + id));

        reservation.setNomClient(dto.getNomClient());
        reservation.setEmailClient(dto.getEmailClient());
        reservation.setDateReservation(dto.getDateReservation());
        reservation.setStatut(dto.getStatut());

        return toDTO(reservationOffreRepository.save(reservation));
    }

    @Override
    @Transactional
    public void deleteReservation(Integer id) {
        if (!reservationOffreRepository.existsById(id)) {
            throw new RessourceNotFoundException(
                    "Réservation introuvable avec l'id : " + id);
        }
        reservationOffreRepository.deleteById(id);
    }

    // ─── Recherches ───────────────────────────────────────────────────────────

    @Override
    public List<ReservationResponseDTO> getReservationsByStatut(StatutReservation statut) {
        return reservationOffreRepository.findByStatut(statut, Pageable.unpaged())
                .getContent().stream().map(this::toDTO).toList();
    }

    @Override
    public List<ReservationResponseDTO> getReservationsByClient(String email) {
        return reservationOffreRepository.findByEmailClientIgnoreCase(email)
                .stream().map(this::toDTO).toList();
    }
}