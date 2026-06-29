package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.entities.reservation.ReservationTrain;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.ClasseTrain;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.reservation.ReservationTrainRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service pour la gestion des réservations de TRAIN.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationTrainService implements ReservationService<ReservationTrain> {

    private final ReservationTrainRepo reservationTrainRepo;

    // ==================== CRUD DE BASE ====================

    @Override
    @Transactional
    public ReservationTrain createReservation(ReservationTrain reservation) {
        return reservationTrainRepo.save(reservation);
    }

    @Override
    public ReservationTrain getReservationById(Integer id) {
        return reservationTrainRepo.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Réservation de train non trouvée avec l'ID: " + id
                ));
    }

    @Override
    public List<ReservationTrain> getAllReservations() {
        return reservationTrainRepo.findAll();
    }

    @Override
    @Transactional
    public ReservationTrain updateReservation(Integer id, ReservationTrain reservation) {
        ReservationTrain existingReservation = getReservationById(id);
        return reservationTrainRepo.saveAndFlush(reservation);
    }

    @Override
    @Transactional
    public void deleteReservation(Integer id) {
        if (!reservationTrainRepo.existsById(id)) {
            throw new RessourceNotFoundException(
                    "Impossible de supprimer : Réservation de train non trouvée avec l'ID: " + id
            );
        }
        reservationTrainRepo.deleteById(id);
    }

    // ==================== HISTORIQUE CLIENT ====================

    @Override
    public List<ReservationTrain> getHistoriqueComplet(Integer clientId) {
        return reservationTrainRepo.findByClientIdClientOrderByCreatedAtDesc(clientId);
    }

    @Override
    public List<ReservationTrain> getHistoriquePasse(Integer clientId) {
        LocalDate maintenant = LocalDate.now();
        return reservationTrainRepo.findReservationsPassees(clientId, maintenant);
    }

    @Override
    public List<ReservationTrain> getReservationsAVenir(Integer clientId) {
        LocalDate maintenant = LocalDate.now();
        return reservationTrainRepo.findReservationsAVenir(clientId, maintenant);
    }

    // ==================== FILTRAGE ET RECHERCHE ====================

    @Override
    public List<ReservationTrain> getReservationsParStatut(Integer clientId, StatutReservation statut) {
        return reservationTrainRepo.findByClientIdClientAndStatutReservation(clientId, statut);
    }

    @Override
    public List<ReservationTrain> getReservationsParTrajet(String villeDepart, String villeArrivee) {
        return reservationTrainRepo.findByVilleDeDepartAndVilleArrivee(villeDepart, villeArrivee);
    }

    @Override
    public long countReservationsByClient(Integer clientId) {
        return reservationTrainRepo.countByClientIdClient(clientId);
    }

    // ==================== ACTIONS MÉTIER ====================

    @Override
    @Transactional
    public ReservationTrain confirmerReservation(Integer id) {
        ReservationTrain reservation = getReservationById(id);
        reservation.setStatutReservation(StatutReservation.CONFIRMEE);
        return reservationTrainRepo.save(reservation);
    }

    @Override
    @Transactional
    public ReservationTrain annulerReservation(Integer id) {
        ReservationTrain reservation = getReservationById(id);
        reservation.setStatutReservation(StatutReservation.ANNULEE);
        return reservationTrainRepo.save(reservation);
    }

    // ==================== MÉTHODES SPÉCIFIQUES TRAIN ====================

    public List<ReservationTrain> findByCompagnieTrain(String compagnie) {
        return reservationTrainRepo.findByCompagnieTrain(compagnie);
    }

    public List<ReservationTrain> findByNumeroWagon(String numeroWagon) {
        return reservationTrainRepo.findByNumeroWagon(numeroWagon);
    }

    public List<ReservationTrain> findByClasseTrain(ClasseTrain classeTrain) {
        return reservationTrainRepo.findByClasseTrain(classeTrain);
    }

    public List<ReservationTrain> findByCompagnieTrainAndClasseTrain(String compagnie, ClasseTrain classeTrain) {
        return reservationTrainRepo.findByCompagnieTrainAndClasseTrain(compagnie, classeTrain);
    }

    public long countByCompagnieTrain(String compagnie) {
        return reservationTrainRepo.countByCompagnieTrain(compagnie);
    }

    public long countByNumeroWagon(String numeroWagon) {
        return reservationTrainRepo.countByNumeroWagon(numeroWagon);
    }

    public List<ReservationTrain> findReservationsPremiereClasse(Integer clientId) {
        return reservationTrainRepo.findByClientIdClientAndClasseTrain(clientId, ClasseTrain.PREMIERE);
    }

    public boolean existsByCompagnieTrain(String compagnie) {
        return reservationTrainRepo.existsByCompagnieTrain(compagnie);
    }

    public List<String> findAllCompagniesDistinctes() {
        return reservationTrainRepo.findAllCompagniesDistinctes();
    }

    public List<ReservationTrain> findByCompagnieAndWagon(String compagnie, String numeroWagon) {
        return reservationTrainRepo.findByCompagnieTrainAndNumeroWagon(compagnie, numeroWagon);
    }
}