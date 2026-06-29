package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.entities.reservation.ReservationBus;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.TypeBus;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.reservation.ReservationBusRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service pour la gestion des réservations de BUS.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationBusService implements ReservationService<ReservationBus> {

    private final ReservationBusRepo reservationBusRepo;

    // ==================== CRUD DE BASE ====================

    @Override
    @Transactional
    public ReservationBus createReservation(ReservationBus reservation) {
        return reservationBusRepo.save(reservation);
    }

    @Override
    public ReservationBus getReservationById(Integer id) {
        return reservationBusRepo.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Réservation de bus non trouvée avec l'ID: " + id
                ));
    }

    @Override
    public List<ReservationBus> getAllReservations() {
        return reservationBusRepo.findAll();
    }

    @Override
    @Transactional
    public ReservationBus updateReservation(Integer id, ReservationBus reservation) {
        ReservationBus existingReservation = getReservationById(id);
        return reservationBusRepo.saveAndFlush(reservation);
    }

    @Override
    @Transactional
    public void deleteReservation(Integer id) {
        if (!reservationBusRepo.existsById(id)) {
            throw new RessourceNotFoundException(
                    "Impossible de supprimer : Réservation de bus non trouvée avec l'ID: " + id
            );
        }
        reservationBusRepo.deleteById(id);
    }

    // ==================== HISTORIQUE CLIENT ====================

    @Override
    public List<ReservationBus> getHistoriqueComplet(Integer clientId) {
        return reservationBusRepo.findByClientIdClientOrderByCreatedAtDesc(clientId);
    }

    @Override
    public List<ReservationBus> getHistoriquePasse(Integer clientId) {
        LocalDate maintenant = LocalDate.now();
        return reservationBusRepo.findReservationsPassees(clientId, maintenant);
    }

    @Override
    public List<ReservationBus> getReservationsAVenir(Integer clientId) {
        LocalDate maintenant = LocalDate.now();
        return reservationBusRepo.findReservationsAVenir(clientId, maintenant);
    }

    // ==================== FILTRAGE ET RECHERCHE ====================

    @Override
    public List<ReservationBus> getReservationsParStatut(Integer clientId, StatutReservation statut) {
        return reservationBusRepo.findByClientIdClientAndStatutReservation(clientId, statut);
    }

    @Override
    public List<ReservationBus> getReservationsParTrajet(String villeDepart, String villeArrivee) {
        return reservationBusRepo.findByVilleDeDepartAndVilleArrivee(villeDepart, villeArrivee);
    }

    @Override
    public long countReservationsByClient(Integer clientId) {
        return reservationBusRepo.countByClientIdClient(clientId);
    }

    // ==================== ACTIONS MÉTIER ====================

    @Override
    @Transactional
    public ReservationBus confirmerReservation(Integer id) {
        ReservationBus reservation = getReservationById(id);
        reservation.setStatutReservation(StatutReservation.CONFIRMEE);
        return reservationBusRepo.save(reservation);
    }

    @Override
    @Transactional
    public ReservationBus annulerReservation(Integer id) {
        ReservationBus reservation = getReservationById(id);
        reservation.setStatutReservation(StatutReservation.ANNULEE);
        return reservationBusRepo.save(reservation);
    }

    // ==================== MÉTHODES SPÉCIFIQUES BUS ====================

    public List<ReservationBus> findByCompagnieBus(String compagnie) {
        return reservationBusRepo.findByCompagnieBus(compagnie);
    }

    public List<ReservationBus> findByTypeBus(TypeBus typeBus) {
        return reservationBusRepo.findByTypeBus(typeBus);
    }

    public List<ReservationBus> findByClimatisation(Boolean hasClimatisation) {
        return reservationBusRepo.findByClimatisation(hasClimatisation);
    }

    public List<ReservationBus> findByCompagnieBusAndTypeBus(String compagnie, TypeBus typeBus) {
        return reservationBusRepo.findByCompagnieBusAndTypeBus(compagnie, typeBus);
    }

    public long countByCompagnieBus(String compagnie) {
        return reservationBusRepo.countByCompagnieBus(compagnie);
    }

    public List<ReservationBus> findReservationsVipClimatises(Integer clientId) {
        return reservationBusRepo.findByClientIdClientAndTypeBusAndClimatisation(
                clientId,
                TypeBus.VIP,
                true
        );
    }

    public boolean existsByCompagnieBus(String compagnie) {
        return reservationBusRepo.existsByCompagnieBus(compagnie);
    }
}