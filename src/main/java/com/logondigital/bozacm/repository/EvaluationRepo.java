package com.logondigital.bozacm.repository;

import com.logondigital.bozacm.entities.Evaluation;
import com.logondigital.bozacm.entities.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepo extends JpaRepository<Evaluation, Integer> {
    Optional<Evaluation> findById(@Param("idEvaluation") Integer idEvaluation);

    List<Evaluation> findByTrajet(Trajet trajet);
}

