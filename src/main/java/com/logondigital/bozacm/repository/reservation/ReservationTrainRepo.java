package com.logondigital.bozacm.repository.reservation;

import com.logondigital.bozacm.entities.reservation.ReservationTrain;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.ClasseTrain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository pour les réservations de train.
 */
@Repository
public interface ReservationTrainRepo extends JpaRepository<ReservationTrain, Integer> {

    List<ReservationTrain> findByClientIdClientOrderByCreatedAtDesc(Integer clientId);

    List<ReservationTrain> findByClientIdClientAndStatutReservation(Integer clientId, StatutReservation statut);

    @Query("SELECT r FROM ReservationTrain r WHERE r.client.idClient = :clientId AND r.offre.dateDepart < :now ORDER BY r.offre.dateDepart DESC")
    List<ReservationTrain> findReservationsPassees(@Param("clientId") Integer clientId, @Param("now") LocalDate now);

    @Query("SELECT r FROM ReservationTrain r WHERE r.client.idClient = :clientId AND r.offre.dateDepart >= :now ORDER BY r.offre.dateDepart ASC")
    List<ReservationTrain> findReservationsAVenir(@Param("clientId") Integer clientId, @Param("now") LocalDate now);

    @Query("SELECT r FROM ReservationTrain r WHERE LOWER(r.offre.trajet.depart) = LOWER(:villeDepart)")
    List<ReservationTrain> findByVilleDeDepart(@Param("villeDepart") String villeDepart);

    @Query("SELECT r FROM ReservationTrain r WHERE LOWER(r.offre.trajet.depart) = LOWER(:villeDepart) AND LOWER(r.offre.trajet.arrivee) = LOWER(:villeArrivee)")
    List<ReservationTrain> findByVilleDeDepartAndVilleArrivee(@Param("villeDepart") String villeDepart, @Param("villeArrivee") String villeArrivee);

    long countByClientIdClient(Integer clientId);

    // ==================== MÉTHODES SPÉCIFIQUES AUX TRAINS ====================

    List<ReservationTrain> findByCompagnieTrain(String compagnie);

    List<ReservationTrain> findByClasseTrain(ClasseTrain classe);

    List<ReservationTrain> findByNumeroWagon(String numeroWagon);

    @Query("SELECT r FROM ReservationTrain r WHERE LOWER(r.offre.trajet.depart) = LOWER(:villeDepart) AND LOWER(r.offre.trajet.arrivee) = LOWER(:villeArrivee) AND r.classeTrain = :classe")
    List<ReservationTrain> findByVilleDeDepartAndVilleArriveeAndClasseTrain(
            @Param("villeDepart") String villeDepart,
            @Param("villeArrivee") String villeArrivee,
            @Param("classe") ClasseTrain classe
    );

    List<ReservationTrain> findByClientIdClientAndCompagnieTrain(Integer clientId, String compagnie);

    List<ReservationTrain> findByClientIdClientAndClasseTrain(Integer clientId, ClasseTrain classe);

    long countByCompagnieTrain(String compagnie);

    long countByClasseTrain(ClasseTrain classe);

    long countByNumeroWagon(String numeroWagon);

    @Query("SELECT DISTINCT r.compagnieTrain FROM ReservationTrain r")
    List<String> findAllCompagniesDistinctes();

    List<ReservationTrain> findByCompagnieTrainAndClasseTrain(String compagnie, ClasseTrain classeTrain);

    boolean existsByCompagnieTrain(String compagnie);

    List<ReservationTrain> findByCompagnieTrainAndNumeroWagon(String compagnie, String numeroWagon);
}