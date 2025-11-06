package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.entities.reservation.ReservationBus;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.TypeBus;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.reservation.ReservationBusRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service pour la gestion des réservations de BUS.

 * Implémente l'interface ReservationService avec le type ReservationBus.
 * Contient la logique métier spécifique aux réservations de bus :
 * - CRUD de base
 * - Historique client
 * - Recherches spécifiques (compagnie, type, climatisation)
 * - Actions métier (confirmer, annuler)

 * Utilise @Transactional pour garantir la cohérence des données.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  // Par défaut, toutes les méthodes sont en lecture seule
public class ReservationBusService implements ReservationService<ReservationBus> {

    private final ReservationBusRepo reservationBusRepo;

    // ===========================================================
    // ==========        CRUD DE BASE                       ======
    // ===========================================================

    /**
     * Crée une nouvelle réservation de bus.
     * La date de création et le statut initial (EN_ATTENTE) sont gérés automatiquement
     * via @PrePersist dans l'entité.
     *
     * @param reservation la réservation à créer
     * @return la réservation créée avec son ID généré
     */
    @Override
    @Transactional  // Méthode d'écriture : transaction en lecture/écriture
    public ReservationBus createReservation(ReservationBus reservation) {
        return reservationBusRepo.save(reservation);
    }

    /**
     * Récupère une réservation de bus par son identifiant.
     *
     * @param id l'identifiant unique de la réservation
     * @return la réservation trouvée
     * @throws RessourceNotFoundException si la réservation n'existe pas
     */
    @Override
    public ReservationBus getReservationById(Integer id) {
        return reservationBusRepo.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Réservation de bus non trouvée avec l'ID: " + id
                ));
    }

    /**
     * Récupère toutes les réservations de bus.
     *
     * @return liste de toutes les réservations de bus
     */
    @Override
    public List<ReservationBus> getAllReservations() {
        return reservationBusRepo.findAll();
    }

    /**
     * Met à jour une réservation de bus existante.
     * La date de mise à jour est gérée automatiquement via @PreUpdate.
     *
     * @param id l'identifiant de la réservation à modifier
     * @param reservation la réservation avec les nouvelles données
     * @return la réservation mise à jour
     * @throws RessourceNotFoundException si la réservation n'existe pas
     */
    @Override
    @Transactional
    public ReservationBus updateReservation(Integer id, ReservationBus reservation) {
        // Vérifier que la réservation existe
        ReservationBus existingReservation = getReservationById(id);

        // Mettre à jour les champs (les champs non-null ont déjà été appliqués par le mapper)
        // Note: Le mapper a déjà fait le travail de mise à jour partielle

        // Sauvegarder et retourner
        return reservationBusRepo.saveAndFlush(reservation);
    }

    /**
     * Supprime une réservation de bus par son identifiant.
     *
     * @param id l'identifiant de la réservation à supprimer
     * @throws RessourceNotFoundException si la réservation n'existe pas
     */
    @Override
    @Transactional
    public void deleteReservation(Integer id) {
        // Vérifier que la réservation existe avant de supprimer
        if (!reservationBusRepo.existsById(id)) {
            throw new RessourceNotFoundException(
                    "Impossible de supprimer : Réservation de bus non trouvée avec l'ID: " + id
            );
        }
        reservationBusRepo.deleteById(id);
    }

    // ===========================================================
    // ==========        HISTORIQUE CLIENT                  ======
    // ===========================================================

    /**
     * Récupère l'historique COMPLET des réservations de bus d'un client.
     * Trié par date de création décroissante (plus récent en premier).
     *
     * @param clientId l'identifiant du client
     * @return liste de toutes les réservations de bus du client
     */
    @Override
    public List<ReservationBus> getHistoriqueComplet(Integer clientId) {
        return reservationBusRepo.findByClientIdClientOrderByCreatedAtDesc(clientId);
    }

    /**
     * Récupère les réservations de bus PASSÉES d'un client.
     * Filtre : dateDepart < maintenant
     *
     * @param clientId l'identifiant du client
     * @return liste des réservations de bus passées
     */
    @Override
    public List<ReservationBus> getHistoriquePasse(Integer clientId) {
        LocalDateTime maintenant = LocalDateTime.now();
        return reservationBusRepo.findReservationsPassees(clientId, maintenant);
    }

    /**
     * Récupère les réservations de bus À VENIR d'un client.
     * Filtre : dateDepart >= maintenant
     * Trié par date de départ croissante (plus proche en premier).
     *
     * @param clientId l'identifiant du client
     * @return liste des réservations de bus à venir
     */
    @Override
    public List<ReservationBus> getReservationsAVenir(Integer clientId) {
        LocalDateTime maintenant = LocalDateTime.now();
        return reservationBusRepo.findReservationsAVenir(clientId, maintenant);
    }

    // ===========================================================
    // ==========        FILTRAGE ET RECHERCHE              ======
    // ===========================================================

    /**
     * Récupère les réservations de bus d'un client par statut.
     *
     * @param clientId l'identifiant du client
     * @param statut le statut recherché (EN_ATTENTE, CONFIRMEE, ANNULEE, COMPLETEE)
     * @return liste des réservations de bus avec ce statut
     */
    @Override
    public List<ReservationBus> getReservationsParStatut(Integer clientId, StatutReservation statut) {
        return reservationBusRepo.findByClientIdClientAndStatutReservation(clientId, statut);
    }

    /**
     * Recherche les réservations de bus pour un trajet spécifique.
     *
     * @param villeDepart la ville de départ
     * @param villeArrivee la ville d'arrivée
     * @return liste des réservations de bus pour ce trajet
     */
    @Override
    public List<ReservationBus> getReservationsParTrajet(String villeDepart, String villeArrivee) {
        return reservationBusRepo.findByVilleDeDepartAndVilleArrivee(villeDepart, villeArrivee);
    }

    /**
     * Compte le nombre total de réservations de bus d'un client.
     *
     * @param clientId l'identifiant du client
     * @return le nombre de réservations de bus
     */
    @Override
    public long countReservationsByClient(Integer clientId) {
        return reservationBusRepo.countByClientIdClient(clientId);
    }

    // ===========================================================
    // ==========        ACTIONS MÉTIER                     ======
    // ===========================================================

    /**
     * Confirme une réservation de bus (change le statut à CONFIRMEE).
     *
     * @param id l'identifiant de la réservation à confirmer
     * @return la réservation de bus confirmée
     * @throws RessourceNotFoundException si la réservation n'existe pas
     */
    @Override
    @Transactional
    public ReservationBus confirmerReservation(Integer id) {
        ReservationBus reservation = getReservationById(id);
        reservation.setStatutReservation(StatutReservation.CONFIRMEE);
        return reservationBusRepo.save(reservation);
    }

    /**
     * Annule une réservation de bus (change le statut à ANNULEE).
     *
     * @param id l'identifiant de la réservation à annuler
     * @return la réservation de bus annulée
     * @throws RessourceNotFoundException si la réservation n'existe pas
     */
    @Override
    @Transactional
    public ReservationBus annulerReservation(Integer id) {
        ReservationBus reservation = getReservationById(id);
        reservation.setStatutReservation(StatutReservation.ANNULEE);
        return reservationBusRepo.save(reservation);
    }

    // ===========================================================
    // ==========        MÉTHODES SPÉCIFIQUES BUS           ======
    // ===========================================================

    /**
     * Recherche les réservations par compagnie de bus.
     *
     * @param compagnie le nom de la compagnie (ex: "Touristique Express")
     * @return liste des réservations pour cette compagnie
     */
    public List<ReservationBus> findByCompagnieBus(String compagnie) {
        return reservationBusRepo.findByCompagnieBus(compagnie);
    }

    /**
     * Recherche les réservations par type de bus.
     *
     * @param typeBus le type de bus (STANDARD ou VIP)
     * @return liste des réservations pour ce type
     */
    public List<ReservationBus> findByTypeBus(TypeBus typeBus) {
        return reservationBusRepo.findByTypeBus(typeBus);
    }

    /**
     * Recherche les réservations selon la disponibilité de la climatisation.
     *
     * @param hasClimatisation true pour bus climatisés, false sinon
     * @return liste des réservations selon le critère de climatisation
     */
    public List<ReservationBus> findByClimatisation(Boolean hasClimatisation) {
        return reservationBusRepo.findByClimatisation(hasClimatisation);
    }

    /**
     * Recherche les réservations par compagnie ET type de bus.
     *
     * @param compagnie le nom de la compagnie
     * @param typeBus le type de bus
     * @return liste des réservations correspondant aux deux critères
     */
    public List<ReservationBus> findByCompagnieBusAndTypeBus(String compagnie, TypeBus typeBus) {
        return reservationBusRepo.findByCompagnieBusAndTypeBus(compagnie, typeBus);
    }

    /**
     * Compte le nombre de réservations pour une compagnie donnée.
     *
     * @param compagnie le nom de la compagnie
     * @return le nombre de réservations
     */
    public long countByCompagnieBus(String compagnie) {
        return reservationBusRepo.countByCompagnieBus(compagnie);
    }

    /**
     * Recherche les réservations VIP avec climatisation pour un client.
     * Exemple de méthode métier combinant plusieurs critères.
     *
     * @param clientId l'identifiant du client
     * @return liste des réservations VIP climatisées du client
     */
    public List<ReservationBus> findReservationsVipClimatises(Integer clientId) {
        return reservationBusRepo.findByClientIdClientAndTypeBusAndClimatisation(
                clientId,
                TypeBus.VIP,
                true
        );
    }

    /**
     * Vérifie si une compagnie existe dans les réservations.
     *
     * @param compagnie le nom de la compagnie
     * @return true si au moins une réservation existe pour cette compagnie
     */
    public boolean existsByCompagnieBus(String compagnie) {
        return reservationBusRepo.existsByCompagnieBus(compagnie);
    }
}