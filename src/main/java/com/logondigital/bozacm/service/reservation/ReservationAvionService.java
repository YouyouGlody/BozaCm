package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.entities.reservation.ReservationAvion;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.ClasseAvion;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.reservation.ReservationAvionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service pour la gestion des réservations d'AVION.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationAvionService implements ReservationService<ReservationAvion> {

    private final ReservationAvionRepo reservationAvionRepo;

    // ==================== CRUD DE BASE ====================

    @Override
    @Transactional
    public ReservationAvion createReservation(ReservationAvion reservation) {
        return reservationAvionRepo.save(reservation);
    }

    @Override
    public ReservationAvion getReservationById(Integer id) {
        return reservationAvionRepo.findById(id).orElseThrow(
                () -> new RessourceNotFoundException(
                        "Réservation d'avion non trouvée avec l'ID: " + id
                ));
    }

    @Override
    public List<ReservationAvion> getAllReservations() {
        return reservationAvionRepo.findAll();
    }

    @Override
    @Transactional
    public ReservationAvion updateReservation(Integer id, ReservationAvion reservation) {
        ReservationAvion existingReservation = getReservationById(id);
        return reservationAvionRepo.saveAndFlush(reservation);
    }

    @Override
    @Transactional
    public void deleteReservation(Integer id) {
        if (!reservationAvionRepo.existsById(id)) {
            throw new RessourceNotFoundException(
                    "Impossible de supprimer : Réservation d'avion non trouvée avec l'ID: " + id
            );
        }
        reservationAvionRepo.deleteById(id);
    }

    // ==================== HISTORIQUE CLIENT ====================

    @Override
    public List<ReservationAvion> getHistoriqueComplet(Integer clientId) {
        return reservationAvionRepo.findByClientIdClientOrderByCreatedAtDesc(clientId);
    }

    @Override
    public List<ReservationAvion> getHistoriquePasse(Integer clientId) {
        LocalDate maintenant = LocalDate.now();
        return reservationAvionRepo.findReservationsPassees(clientId, maintenant);
    }

    @Override
    public List<ReservationAvion> getReservationsAVenir(Integer clientId) {
        LocalDate maintenant = LocalDate.now();
        return reservationAvionRepo.findReservationsAVenir(clientId, maintenant);
    }

    // ==================== FILTRAGE ET RECHERCHE ====================

    @Override
    public List<ReservationAvion> getReservationsParStatut(Integer clientId, StatutReservation statut) {
        return reservationAvionRepo.findByClientIdClientAndStatutReservation(clientId, statut);
    }

    @Override
    public List<ReservationAvion> getReservationsParTrajet(String villeDepart, String villeArrivee) {
        return reservationAvionRepo.findByVilleDeDepartAndVilleArrivee(villeDepart, villeArrivee);
    }

    @Override
    public long countReservationsByClient(Integer clientId) {
        return reservationAvionRepo.countByClientIdClient(clientId);
    }

    // ==================== ACTIONS MÉTIER ====================

    @Override
    @Transactional
    public ReservationAvion confirmerReservation(Integer id) {
        ReservationAvion reservation = getReservationById(id);
        reservation.setStatutReservation(StatutReservation.CONFIRMEE);
        return reservationAvionRepo.save(reservation);
    }

    @Override
    @Transactional
    public ReservationAvion annulerReservation(Integer id) {
        ReservationAvion reservation = getReservationById(id);
        reservation.setStatutReservation(StatutReservation.ANNULEE);
        return reservationAvionRepo.save(reservation);
    }

    // ==================== MÉTHODES SPÉCIFIQUES AVION ====================

    public List<ReservationAvion> findByCompagnieAerienne(String compagnie) {
        return reservationAvionRepo.findByCompagnieAerienne(compagnie);
    }

    public List<ReservationAvion> findByNumeroVol(String numeroVol) {
        return reservationAvionRepo.findByNumeroVol(numeroVol);
    }

    public List<ReservationAvion> findByClasseAvion(ClasseAvion classeAvion) {
        return reservationAvionRepo.findByClasseAvion(classeAvion);
    }

    public List<ReservationAvion> findByNumeroTerminal(String numeroTerminal) {
        return reservationAvionRepo.findByNumeroTerminal(numeroTerminal);
    }

    public List<ReservationAvion> findByCompagnieAerienneAndClasseAvion(String compagnie, ClasseAvion classeAvion) {
        return reservationAvionRepo.findByCompagnieAerienneAndClasseAvion(compagnie, classeAvion);
    }

    public long countByCompagnieAerienne(String compagnie) {
        return reservationAvionRepo.countByCompagnieAerienne(compagnie);
    }

    public long countByNumeroVol(String numeroVol) {
        return reservationAvionRepo.countByNumeroVol(numeroVol);
    }

    public List<ReservationAvion> findReservationsPremium(Integer clientId) {
        List<ReservationAvion> affaires = reservationAvionRepo.findByClientIdClientAndClasseAvion(
                clientId, ClasseAvion.AFFAIRES
        );
        List<ReservationAvion> premiere = reservationAvionRepo.findByClientIdClientAndClasseAvion(
                clientId, ClasseAvion.PREMIERE
        );
        affaires.addAll(premiere);
        return affaires;
    }

    public List<ReservationAvion> findByPoidsMaxBagagesGreaterThanEqual(Integer poidsMin) {
        return reservationAvionRepo.findByPoidsMaxBagagesGreaterThanEqual(poidsMin);
    }

    public boolean existsByCompagnieAerienne(String compagnie) {
        return reservationAvionRepo.existsByCompagnieAerienne(compagnie);
    }

    public boolean existsByNumeroVol(String numeroVol) {
        return reservationAvionRepo.existsByNumeroVol(numeroVol);
    }

    public List<String> findAllCompagniesDistinctes() {
        return reservationAvionRepo.findAllCompagniesDistinctes();
    }

    public List<String> findVolsByCompagnie(String compagnie) {
        return reservationAvionRepo.findVolsByCompagnie(compagnie);
    }

    public long countByClasseAvion(ClasseAvion classeAvion) {
        return reservationAvionRepo.countByClasseAvion(classeAvion);
    }
}