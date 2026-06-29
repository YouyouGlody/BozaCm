package com.logondigital.bozacm.repository.reservation;

import com.logondigital.bozacm.entities.reservation.ReservationBus;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.TypeBus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository pour les réservations de bus.
 */
@Repository
public interface ReservationBusRepo extends JpaRepository<ReservationBus, Integer> {

    List<ReservationBus> findByClientIdClientOrderByCreatedAtDesc(Integer clientId);

    List<ReservationBus> findByClientIdClientAndStatutReservation(Integer clientId, StatutReservation statut);

    @Query("SELECT r FROM ReservationBus r WHERE r.client.idClient = :clientId AND r.offre.dateDepart < :now ORDER BY r.offre.dateDepart DESC")
    List<ReservationBus> findReservationsPassees(@Param("clientId") Integer clientId, @Param("now") LocalDate now);

    @Query("SELECT r FROM ReservationBus r WHERE r.client.idClient = :clientId AND r.offre.dateDepart >= :now ORDER BY r.offre.dateDepart ASC")
    List<ReservationBus> findReservationsAVenir(@Param("clientId") Integer clientId, @Param("now") LocalDate now);

    @Query("SELECT r FROM ReservationBus r WHERE LOWER(r.offre.trajet.depart) = LOWER(:villeDepart)")
    List<ReservationBus> findByVilleDeDepart(@Param("villeDepart") String villeDepart);

    @Query("SELECT r FROM ReservationBus r WHERE LOWER(r.offre.trajet.depart) = LOWER(:villeDepart) AND LOWER(r.offre.trajet.arrivee) = LOWER(:villeArrivee)")
    List<ReservationBus> findByVilleDeDepartAndVilleArrivee(@Param("villeDepart") String villeDepart, @Param("villeArrivee") String villeArrivee);

    long countByClientIdClient(Integer clientId);

    // ==================== MÉTHODES SPÉCIFIQUES AUX BUS ====================

    List<ReservationBus> findByCompagnieBus(String compagnie);

    List<ReservationBus> findByTypeBus(TypeBus typeBus);

    List<ReservationBus> findByClimatisation(Boolean climatisation);

    List<ReservationBus> findByTypeBusAndClimatisation(TypeBus typeBus, Boolean climatisation);

    @Query("SELECT r FROM ReservationBus r WHERE LOWER(r.offre.trajet.depart) = LOWER(:villeDepart) AND LOWER(r.offre.trajet.arrivee) = LOWER(:villeArrivee) AND r.typeBus = :typeBus")
    List<ReservationBus> findByVilleDeDepartAndVilleArriveeAndTypeBus(
            @Param("villeDepart") String villeDepart,
            @Param("villeArrivee") String villeArrivee,
            @Param("typeBus") TypeBus typeBus
    );

    List<ReservationBus> findByClientIdClientAndCompagnieBus(Integer clientId, String compagnie);

    long countByCompagnieBus(String compagnie);

    long countByTypeBus(TypeBus typeBus);

    List<ReservationBus> findByCompagnieBusAndTypeBus(String compagnie, TypeBus typeBus);

    List<ReservationBus> findByClientIdClientAndTypeBusAndClimatisation(Integer clientId, TypeBus typeBus, boolean b);

    boolean existsByCompagnieBus(String compagnie);
}