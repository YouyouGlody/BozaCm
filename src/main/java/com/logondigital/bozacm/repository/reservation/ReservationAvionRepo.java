package com.logondigital.bozacm.repository.reservation;

import com.logondigital.bozacm.entities.reservation.ReservationAvion;
import com.logondigital.bozacm.enums.StatutReservation;
import com.logondigital.bozacm.enums.transport.ClasseAvion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository pour les réservations d'avion.
 */
@Repository
public interface ReservationAvionRepo extends JpaRepository<ReservationAvion, Integer> {

    List<ReservationAvion> findByClientIdClientOrderByCreatedAtDesc(Integer clientId);

    List<ReservationAvion> findByClientIdClientAndStatutReservation(Integer clientId, StatutReservation statut);

    @Query("SELECT r FROM ReservationAvion r WHERE r.client.idClient = :clientId AND r.offre.dateDepart < :now ORDER BY r.offre.dateDepart DESC")
    List<ReservationAvion> findReservationsPassees(@Param("clientId") Integer clientId, @Param("now") LocalDate now);

    @Query("SELECT r FROM ReservationAvion r WHERE r.client.idClient = :clientId AND r.offre.dateDepart >= :now ORDER BY r.offre.dateDepart ASC")
    List<ReservationAvion> findReservationsAVenir(@Param("clientId") Integer clientId, @Param("now") LocalDate now);

    @Query("SELECT r FROM ReservationAvion r WHERE LOWER(r.offre.trajet.depart) = LOWER(:villeDepart)")
    List<ReservationAvion> findByVilleDeDepart(@Param("villeDepart") String villeDepart);

    @Query("SELECT r FROM ReservationAvion r WHERE LOWER(r.offre.trajet.depart) = LOWER(:villeDepart) AND LOWER(r.offre.trajet.arrivee) = LOWER(:villeArrivee)")
    List<ReservationAvion> findByVilleDeDepartAndVilleArrivee(@Param("villeDepart") String villeDepart, @Param("villeArrivee") String villeArrivee);

    long countByClientIdClient(Integer clientId);

    // ==================== MÉTHODES SPÉCIFIQUES AUX AVIONS ====================

    List<ReservationAvion> findByCompagnieAerienne(String compagnie);

    List<ReservationAvion> findByNumeroVol(String numeroVol);

    List<ReservationAvion> findByClasseAvion(ClasseAvion classe);

    List<ReservationAvion> findByNumeroTerminal(String terminal);

    @Query("SELECT r FROM ReservationAvion r WHERE r.poidsMaxBagages >= :poidsMin")
    List<ReservationAvion> findByPoidsMaxBagagesGreaterThanEqual(@Param("poidsMin") Integer poidsMin);

    @Query("SELECT r FROM ReservationAvion r WHERE r.classeAvion = :classe AND r.poidsMaxBagages >= :poidsMin")
    List<ReservationAvion> findByClasseAvionAndPoidsMaxBagages(
            @Param("classe") ClasseAvion classe,
            @Param("poidsMin") Integer poidsMin
    );

    @Query("SELECT r FROM ReservationAvion r WHERE LOWER(r.offre.trajet.depart) = LOWER(:villeDepart) AND LOWER(r.offre.trajet.arrivee) = LOWER(:villeArrivee) AND r.classeAvion = :classe")
    List<ReservationAvion> findByVilleDeDepartAndVilleArriveeAndClasseAvion(
            @Param("villeDepart") String villeDepart,
            @Param("villeArrivee") String villeArrivee,
            @Param("classe") ClasseAvion classe
    );

    @Query("SELECT r FROM ReservationAvion r WHERE r.numeroVol = :numeroVol AND r.offre.dateDepart = :dateDepart")
    List<ReservationAvion> findByNumeroVolAndDate(
            @Param("numeroVol") String numeroVol,
            @Param("dateDepart") LocalDate dateDepart
    );

    List<ReservationAvion> findByClientIdClientAndCompagnieAerienne(Integer clientId, String compagnie);

    List<ReservationAvion> findByClientIdClientAndClasseAvion(Integer clientId, ClasseAvion classe);

    long countByCompagnieAerienne(String compagnie);

    long countByClasseAvion(ClasseAvion classe);

    long countByNumeroVol(String numeroVol);

    long countByNumeroTerminal(String terminal);

    boolean existsByCompagnieAerienne(String compagnie);

    boolean existsByNumeroVol(String numeroVol);

    @Query("SELECT DISTINCT r.compagnieAerienne FROM ReservationAvion r")
    List<String> findAllCompagniesDistinctes();

    @Query("SELECT DISTINCT r.numeroVol FROM ReservationAvion r WHERE r.compagnieAerienne = :compagnie")
    List<String> findVolsByCompagnie(@Param("compagnie") String compagnie);

    List<ReservationAvion> findByCompagnieAerienneAndClasseAvion(String compagnie, ClasseAvion classeAvion);
}