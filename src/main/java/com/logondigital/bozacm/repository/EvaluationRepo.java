package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Evaluation;
import com.logondigital.bozacm.entities.Trajet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepo extends JpaRepository<Evaluation, Integer> {
    Optional<Evaluation> findById(@Param("idEvaluation") Integer idEvaluation);

    List<Evaluation> findByTrajet(Trajet trajet);

    @Query("SELECT e FROM Evaluation e WHERE LOWER(e.commentaire) LIKE LOWER(CONCAT('%', :motCle, '%'))")
    Page<Evaluation> findByCommentaireContainingIgnoreCase(@Param("motCle") String motCle, Pageable pageable);

}

