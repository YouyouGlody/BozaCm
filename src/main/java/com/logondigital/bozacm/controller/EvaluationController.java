package com.logondigital.bozacm.controller;

import com.logondigital.bozacm.DTO.EvaluationReq;
import com.logondigital.bozacm.DTO.EvaluationResp;
import com.logondigital.bozacm.entities.Evaluation;
import com.logondigital.bozacm.entities.Trajet;
import com.logondigital.bozacm.repository.EvaluationRepo;
import com.logondigital.bozacm.repository.TrajetRepo;
import com.logondigital.bozacm.exception.ResourceNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/evaluations")
public class EvaluationController {

    private final EvaluationRepo evaluationRepo;
    private final TrajetRepo trajetRepo;

    public EvaluationController(EvaluationRepo evaluationRepo, TrajetRepo trajetRepo) {
        this.evaluationRepo = evaluationRepo;
        this.trajetRepo = trajetRepo;
    }


    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody EvaluationReq req) {

        Trajet trajet = trajetRepo.findById(req.getTrajetId())
                .orElseThrow(() -> new ResourceNotFoundException("Trajet introuvable"));

        Evaluation evaluation = new Evaluation();
        evaluation.setNote(req.getNote());
        evaluation.setCommentaire(req.getCommentaire());
        evaluation.setDateEvaluation(new Date());
        evaluation.setTrajet(trajet);

        evaluationRepo.save(evaluation);

        return ResponseEntity.status(201).body("Evaluation créée !");
    }


    @GetMapping("/get_all")
    public ResponseEntity<List<EvaluationResp>> getAll() {

        List<EvaluationResp> list = evaluationRepo.findAll()
                .stream()
                .map(evaluation -> new EvaluationResp(
                        evaluation.getIdEvaluation(),
                        evaluation.getNote(),
                        evaluation.getCommentaire()
                ))
                .toList();

        return ResponseEntity.status(200).body(list);
    }

    @GetMapping("/get_by_id/{idEvaluation}")
    public ResponseEntity<EvaluationResp> getByIdEvaluation(@PathVariable Integer idEvaluation) {

        Evaluation evaluation = evaluationRepo.findById(idEvaluation)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation introuvable"));

        EvaluationResp evaluationResp = new EvaluationResp(
                evaluation.getIdEvaluation(),
                evaluation.getNote(),
                evaluation.getCommentaire()
        );

        return ResponseEntity.status(200).body(evaluationResp);
    }


    @DeleteMapping("/delete/{idEvaluation}")
    public ResponseEntity<String> delete(@PathVariable Integer idEvaluation) {

        evaluationRepo.deleteById(idEvaluation);

        return ResponseEntity.status(202).body("Evaluation supprimée !");
    }
}