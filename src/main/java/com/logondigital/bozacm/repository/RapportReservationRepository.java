package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.reservation.Reservation;
import com.logondigital.bozacm.enums.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository dédié aux statistiques globales du rapport.
 * Interroge l'entité mère Reservation (polymorphe : Bus + Train + Avion réunis)
 * grâce à l'héritage JOINED. Les infos de voyage sont lues depuis l'offre liée.
 */
@Repository
public interface RapportReservationRepository extends JpaRepository<Reservation, Integer> {

    /** Nombre total de réservations (tous types confondus) */
    @Query("SELECT COUNT(r) FROM Reservation r")
    Long countTotal();

    /** Nombre de réservations par statut */
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.statutReservation = :statut")
    Long countByStatut(@Param("statut") StatutReservation statut);

    /** Chiffre d'affaires total — somme des prix des offres des réservations CONFIRMEES */
    @Query("SELECT COALESCE(SUM(r.offre.prix), 0) FROM Reservation r WHERE r.statutReservation = com.logondigital.bozacm.enums.StatutReservation.CONFIRMEE")
    Double getTotalChiffreAffaires();

    /** Offre la plus réservée — titre de l'offre avec le plus de réservations */
    @Query("""
            SELECT r.offre.titre FROM Reservation r
            GROUP BY r.offre.titre
            ORDER BY COUNT(r) DESC
            LIMIT 1
            """)
    String findOffreLaPlusReservee();

    /** Agence la plus active — celle avec le plus de réservations confirmées */
    @Query("""
            SELECT r.offre.agence.nom FROM Reservation r
            WHERE r.statutReservation = com.logondigital.bozacm.enums.StatutReservation.CONFIRMEE
            GROUP BY r.offre.agence.nom
            ORDER BY COUNT(r) DESC
            LIMIT 1
            """)
    String findAgenceLaPlusActive();

    /** Trajet le plus emprunté — depart → arrivee avec le plus de réservations */
    @Query("""
            SELECT CONCAT(r.offre.trajet.depart, ' → ', r.offre.trajet.arrivee)
            FROM Reservation r
            GROUP BY r.offre.trajet.depart, r.offre.trajet.arrivee
            ORDER BY COUNT(r) DESC
            LIMIT 1
            """)
    String findTrajetLePlusEmprunte();
}