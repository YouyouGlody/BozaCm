package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.ReservationOffre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationOffreRepo extends JpaRepository<ReservationOffre, Integer> {
    Optional<ReservationOffre> findById(Integer id);


    @Query("SELECT DISTINCT r FROM ReservationOffre r " +
            "LEFT JOIN FETCH r.offre o " +
            "LEFT JOIN FETCH o.agence " +
            "LEFT JOIN FETCH o.trajet")
    Page<ReservationOffre> findAllWithOffreDetailsPaginated(Pageable pageable);


    @Query("SELECT r FROM ReservationOffre r WHERE r.statut = :statut")
    List<ReservationOffre> findByStatut(@Param("statut") String statut);


    @Query("SELECT r FROM ReservationOffre r WHERE r.emailClient = :email")
    List<ReservationOffre> findByEmailClient(@Param("email") String email);


    @Query("SELECT COUNT(r) FROM ReservationOffre r WHERE r.offre.agence.id = :agenceId")
    Long countReservationsByAgenceId(@Param("agenceId") Integer agenceId);


    @Query("SELECT COUNT(r) FROM ReservationOffre r WHERE r.offre.agence.id = :agenceId AND r.statut = :statut")
    Long countReservationsByAgenceIdAndStatut(@Param("agenceId") Integer agenceId, @Param("statut") String statut);


    @Query("SELECT SUM(r.offre.prix) FROM ReservationOffre r WHERE r.offre.agence.id = :agenceId AND r.statut = :statut")
    Double getChiffreAffaireByAgenceId(@Param("agenceId") Integer agenceId, @Param("statut") String statut);
}