package com.logondigital.bozacm.service.Offre;

import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.exceptions.InvalidReservationException;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.OffreRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Tient à jour le nombre de places disponibles d'une offre.
 * Une réservation (non annulée) occupe une place ; l'annulation ou la suppression la libère.
 */
@Service
@RequiredArgsConstructor
public class GestionPlacesService {

    private static final Logger logger = LoggerFactory.getLogger(GestionPlacesService.class);

    private final OffreRepository offreRepository;

    /** Occupe une place de l'offre, ou refuse si l'offre est complète ou déjà partie. */
    @Transactional
    public void reserverPlace(Offre offreReservee) {
        if (offreReservee == null) return;
        Offre offre = charger(offreReservee.getId());

        if (offre.getDateDepart() != null && offre.getDateDepart().isBefore(LocalDate.now())) {
            throw new InvalidReservationException("Impossible de réserver : le départ de cette offre est déjà passé.");
        }
        int disponibles = placesDisponibles(offre);
        if (disponibles <= 0) {
            throw new InvalidReservationException("Cette offre est complète : il n'y a plus de place disponible.");
        }
        offre.setPlacesDisponibles(disponibles - 1);
        offreRepository.save(offre);
    }

    /** Rend une place à l'offre (annulation ou suppression d'une réservation active). */
    @Transactional
    public void libererPlace(Offre offreReservee) {
        if (offreReservee == null) return;
        Offre offre = charger(offreReservee.getId());
        int disponibles = placesDisponibles(offre) + 1;
        offre.setPlacesDisponibles(Math.min(disponibles, offre.getNombrePlaces()));
        offreRepository.save(offre);
    }

    /** Nombre de réservations non annulées sur une offre. */
    public long reservationsActives(Integer offreId) {
        Long total = offreRepository.countReservationsActives(offreId);
        return total == null ? 0 : total;
    }

    /**
     * Au démarrage, recalcule les places disponibles de toutes les offres à partir des réservations
     * existantes (les offres créées avant cette fonctionnalité n'étaient pas décomptées).
     */
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void recalculerToutesLesOffres() {
        offreRepository.findAll().forEach(offre -> {
            int attendu = (int) Math.max(0, offre.getNombrePlaces() - reservationsActives(offre.getId()));
            if (offre.getPlacesDisponibles() == null || offre.getPlacesDisponibles() != attendu) {
                offre.setPlacesDisponibles(attendu);
                offreRepository.save(offre);
            }
        });
        logger.info("Places disponibles des offres recalculées à partir des réservations.");
    }

    private Offre charger(Integer offreId) {
        return offreRepository.findById(offreId)
                .orElseThrow(() -> new RessourceNotFoundException("Offre introuvable avec l'id : " + offreId));
    }

    private int placesDisponibles(Offre offre) {
        return offre.getPlacesDisponibles() != null ? offre.getPlacesDisponibles() : offre.getNombrePlaces();
    }
}
