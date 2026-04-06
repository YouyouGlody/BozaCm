package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.entities.reservation.ReservationTrain;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.ClasseTrain;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.reservation.ReservationTrainRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service pour la gestion des réservations de TRAIN.
 *
 * Implémente l'interface ReservationService avec le type ReservationTrain.
 * Contient la logique métier spécifique aux réservations de train :
 * - CRUD de base
 * - Historique client
 * - Recherches spécifiques (compagnie, wagon, classe)
 * - Actions métier (confirmer, annuler)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationTrainService implements ReservationService<ReservationTrain> {

    private final ReservationTrainRepo reservationTrainRepo;

    // ===========================================================
    // ==========        CRUD DE BASE                       ======
    // ===========================================================

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

    // ===========================================================
    // ==========        HISTORIQUE CLIENT                  ======
    // ===========================================================

    @Override
    public List<ReservationTrain> getHistoriqueComplet(Integer clientId) {
        return reservationTrainRepo.findByClientIdClientOrderByCreatedAtDesc(clientId);
    }

    @Override
    public List<ReservationTrain> getHistoriquePasse(Integer clientId) {
        LocalDateTime maintenant = LocalDateTime.now();
        return reservationTrainRepo.findReservationsPassees(clientId, maintenant);
    }

    @Override
    public List<ReservationTrain> getReservationsAVenir(Integer clientId) {
        LocalDateTime maintenant = LocalDateTime.now();
        return reservationTrainRepo.findReservationsAVenir(clientId, maintenant);
    }

    // ===========================================================
    // ==========        FILTRAGE ET RECHERCHE              ======
    // ===========================================================

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

    // ===========================================================
    // ==========        ACTIONS MÉTIER                     ======
    // ===========================================================

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

    // ===========================================================
    // ==========        MÉTHODES SPÉCIFIQUES TRAIN         ======
    // ===========================================================

    /**
     * Recherche les réservations par compagnie de train.
     *
     * @param compagnie le nom de la compagnie (ex: "CAMRAIL")
     * @return liste des réservations pour cette compagnie
     */
    public List<ReservationTrain> findByCompagnieTrain(String compagnie) {
        return reservationTrainRepo.findByCompagnieTrain(compagnie);
    }

    /**
     * Recherche les réservations par numéro de wagon.
     *
     * @param numeroWagon le numéro du wagon (ex: "A", "B", "1", "2")
     * @return liste des réservations pour ce wagon
     */
    public List<ReservationTrain> findByNumeroWagon(String numeroWagon) {
        return reservationTrainRepo.findByNumeroWagon(numeroWagon);
    }

    /**
     * Recherche les réservations par classe de train.
     *
     * @param classeTrain la classe (PREMIERE ou SECONDE)
     * @return liste des réservations pour cette classe
     */
    public List<ReservationTrain> findByClasseTrain(ClasseTrain classeTrain) {
        return reservationTrainRepo.findByClasseTrain(classeTrain);
    }

    /**
     * Recherche les réservations par compagnie ET classe.
     *
     * @param compagnie le nom de la compagnie
     * @param classeTrain la classe de train
     * @return liste des réservations correspondant aux deux critères
     */
    public List<ReservationTrain> findByCompagnieTrainAndClasseTrain(String compagnie, ClasseTrain classeTrain) {
        return reservationTrainRepo.findByCompagnieTrainAndClasseTrain(compagnie, classeTrain);
    }

    /**
     * Compte le nombre de réservations pour une compagnie donnée.
     *
     * @param compagnie le nom de la compagnie
     * @return le nombre de réservations
     */
    public long countByCompagnieTrain(String compagnie) {
        return reservationTrainRepo.countByCompagnieTrain(compagnie);
    }

    /**
     * Compte le nombre de réservations par wagon.
     *
     * @param numeroWagon le numéro du wagon
     * @return le nombre de réservations pour ce wagon
     */
    public long countByNumeroWagon(String numeroWagon) {
        return reservationTrainRepo.countByNumeroWagon(numeroWagon);
    }

    /**
     * Recherche les réservations première classe pour un client.
     *
     * @param clientId l'identifiant du client
     * @return liste des réservations première classe du client
     */
    public List<ReservationTrain> findReservationsPremiereClasse(Integer clientId) {
        return reservationTrainRepo.findByClientIdClientAndClasseTrain(clientId, ClasseTrain.PREMIERE);
    }

    /**
     * Vérifie si une compagnie existe dans les réservations.
     *
     * @param compagnie le nom de la compagnie
     * @return true si au moins une réservation existe pour cette compagnie
     */
    public boolean existsByCompagnieTrain(String compagnie) {
        return reservationTrainRepo.existsByCompagnieTrain(compagnie);
    }

    /**
     * Récupère toutes les compagnies de train distinctes.
     * Utile pour afficher une liste de compagnies disponibles.
     *
     * @return liste des noms de compagnies uniques
     */
    public List<String> findAllCompagniesDistinctes() {
        return reservationTrainRepo.findAllCompagniesDistinctes();
    }

    /**
     * Récupère toutes les réservations pour un wagon spécifique d'une compagnie.
     * Permet de voir l'occupation d'un wagon.
     *
     * @param compagnie le nom de la compagnie
     * @param numeroWagon le numéro du wagon
     * @return liste des réservations pour ce wagon
     */
    public List<ReservationTrain> findByCompagnieAndWagon(String compagnie, String numeroWagon) {
        return reservationTrainRepo.findByCompagnieTrainAndNumeroWagon(compagnie, numeroWagon);
    }
}