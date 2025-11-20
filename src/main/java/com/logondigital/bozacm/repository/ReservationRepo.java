package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Offre;
import com.logondigital.bozacm.entities.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Integer> {
    Optional<Reservation> findById(Integer id);

    @Query("SELECT DISTINCT r FROM Reservation r " +
            "LEFT JOIN FETCH r.offre o " +
            "LEFT JOIN FETCH o.agence " +
            "LEFT JOIN FETCH o.trajet")
    Page<Reservation> findAllWithOffreDetailsPaginated(Pageable pageable);


    @Query("SELECT r FROM Reservation r WHERE r.statut = :statut")
    List<Reservation> findByStatut(@Param("statut") String statut);


    @Query("SELECT r FROM Reservation r WHERE r.emailClient = :email")
    List<Reservation> findByEmailClient(@Param("email") String email);

}

