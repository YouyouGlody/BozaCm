package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.entities.reservation.ReservationAvion;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.ClasseAvion;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.reservation.ReservationAvionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service pour la gestion des réservations d'AVION.
 *
 * Implémente l'interface ReservationService avec le type ReservationAvion.
 * Contient la logique métier spécifique aux réservations d'avion :
 * - CRUD de base
 * - Historique client
 * - Recherches spécifiques (compagnie, vol, terminal, classe)
 * - Actions métier (confirmer, annuler)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationAvionService implements ReservationService<ReservationAvion> {

    private final ReservationAvionRepo reservationAvionRepo;

    // ===========================================================
    // ==========        CRUD DE BASE                       ======
    // ===========================================================

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

    // ===========================================================
    // ==========        HISTORIQUE CLIENT                  ======
    // ===========================================================

    @Override
    public List<ReservationAvion> getHistoriqueComplet(Integer clientId) {
        return reservationAvionRepo.findByClientIdClientOrderByCreatedAtDesc(clientId);
    }

    @Override
    public List<ReservationAvion> getHistoriquePasse(Integer clientId) {
        LocalDateTime maintenant = LocalDateTime.now();
        return reservationAvionRepo.findReservationsPassees(clientId, maintenant);
    }

    @Override
    public List<ReservationAvion> getReservationsAVenir(Integer clientId) {
        LocalDateTime maintenant = LocalDateTime.now();
        return reservationAvionRepo.findReservationsAVenir(clientId, maintenant);
    }

    // ===========================================================
    // ==========        FILTRAGE ET RECHERCHE              ======
    // ===========================================================

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

    // ===========================================================
    // ==========        ACTIONS MÉTIER                     ======
    // ===========================================================

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

    // ===========================================================
    // ==========        MÉTHODES SPÉCIFIQUES AVION         ======
    // ===========================================================

    /**
     * Recherche les réservations par compagnie aérienne.
     *
     * @param compagnie le nom de la compagnie (ex: "Air France", "Camair-Co")
     * @return liste des réservations pour cette compagnie
     */
    public List<ReservationAvion> findByCompagnieAerienne(String compagnie) {
        return reservationAvionRepo.findByCompagnieAerienne(compagnie);
    }

    /**
     * Recherche les réservations par numéro de vol.
     *
     * @param numeroVol le numéro du vol (ex: "AF1234")
     * @return liste des réservations pour ce vol
     */
    public List<ReservationAvion> findByNumeroVol(String numeroVol) {
        return reservationAvionRepo.findByNumeroVol(numeroVol);
    }

    /**
     * Recherche les réservations par classe d'avion.
     *
     * @param classeAvion la classe (ECONOMIE, AFFAIRES, PREMIERE)
     * @return liste des réservations pour cette classe
     */
    public List<ReservationAvion> findByClasseAvion(ClasseAvion classeAvion) {
        return reservationAvionRepo.findByClasseAvion(classeAvion);
    }

    /**
     * Recherche les réservations par terminal.
     *
     * @param numeroTerminal le numéro du terminal (ex: "2E", "Terminal Sud")
     * @return liste des réservations pour ce terminal
     */
    public List<ReservationAvion> findByNumeroTerminal(String numeroTerminal) {
        return reservationAvionRepo.findByNumeroTerminal(numeroTerminal);
    }

    /**
     * Recherche les réservations par compagnie ET classe.
     *
     * @param compagnie le nom de la compagnie
     * @param classeAvion la classe d'avion
     * @return liste des réservations correspondant aux deux critères
     */
    public List<ReservationAvion> findByCompagnieAerienneAndClasseAvion(String compagnie, ClasseAvion classeAvion) {
        return reservationAvionRepo.findByCompagnieAerienneAndClasseAvion(compagnie, classeAvion);
    }

    /**
     * Compte le nombre de réservations pour une compagnie donnée.
     *
     * @param compagnie le nom de la compagnie
     * @return le nombre de réservations
     */
    public long countByCompagnieAerienne(String compagnie) {
        return reservationAvionRepo.countByCompagnieAerienne(compagnie);
    }

    /**
     * Compte le nombre de réservations pour un vol donné.
     *
     * @param numeroVol le numéro du vol
     * @return le nombre de réservations pour ce vol
     */
    public long countByNumeroVol(String numeroVol) {
        return reservationAvionRepo.countByNumeroVol(numeroVol);
    }

    /**
     * Recherche les réservations classe affaires ou première pour un client.
     *
     * @param clientId l'identifiant du client
     * @return liste des réservations premium du client
     */
    public List<ReservationAvion> findReservationsPremium(Integer clientId) {
        // On va chercher les deux classes séparément et les combiner
        List<ReservationAvion> affaires = reservationAvionRepo.findByClientIdClientAndClasseAvion(
                clientId, ClasseAvion.AFFAIRES
        );
        List<ReservationAvion> premiere = reservationAvionRepo.findByClientIdClientAndClasseAvion(
                clientId, ClasseAvion.PREMIERE
        );

        // Combiner les deux listes
        affaires.addAll(premiere);
        return affaires;
    }

    /**
     * Recherche les réservations par poids de bagages minimum.
     * Utile pour filtrer les réservations avec bagages lourds.
     *
     * @param poidsMin le poids minimum en kg
     * @return liste des réservations avec au moins ce poids de bagages
     */
    public List<ReservationAvion> findByPoidsMaxBagagesGreaterThanEqual(Integer poidsMin) {
        return reservationAvionRepo.findByPoidsMaxBagagesGreaterThanEqual(poidsMin);
    }

    /**
     * Vérifie si une compagnie existe dans les réservations.
     *
     * @param compagnie le nom de la compagnie
     * @return true si au moins une réservation existe pour cette compagnie
     */
    public boolean existsByCompagnieAerienne(String compagnie) {
        return reservationAvionRepo.existsByCompagnieAerienne(compagnie);
    }

    /**
     * Vérifie si un vol existe dans les réservations.
     *
     * @param numeroVol le numéro du vol
     * @return true si au moins une réservation existe pour ce vol
     */
    public boolean existsByNumeroVol(String numeroVol) {
        return reservationAvionRepo.existsByNumeroVol(numeroVol);
    }

    /**
     * Récupère toutes les compagnies aériennes distinctes.
     * Utile pour afficher une liste de compagnies disponibles.
     *
     * @return liste des noms de compagnies uniques
     */
    public List<String> findAllCompagniesDistinctes() {
        return reservationAvionRepo.findAllCompagniesDistinctes();
    }

    /**
     * Récupère tous les vols distincts pour une compagnie.
     * Utile pour afficher les vols disponibles d'une compagnie.
     *
     * @param compagnie le nom de la compagnie
     * @return liste des numéros de vols uniques pour cette compagnie
     */
    public List<String> findVolsByCompagnie(String compagnie) {
        return reservationAvionRepo.findVolsByCompagnie(compagnie);
    }

    /**
     * Récupère le nombre total de réservations par classe.
     * Utile pour des statistiques.
     *
     * @param classeAvion la classe
     * @return le nombre de réservations pour cette classe
     */
    public long countByClasseAvion(ClasseAvion classeAvion) {
        return reservationAvionRepo.countByClasseAvion(classeAvion);
    }
}