package com.logondigital.bozacm.service.evaluation;

import com.logondigital.bozacm.DTO.EvaluationReq;
import com.logondigital.bozacm.DTO.EvaluationResp;
import com.logondigital.bozacm.entities.Evaluation;
import com.logondigital.bozacm.entities.Trajet;
import com.logondigital.bozacm.exception.ResourceNotFoundException;
import com.logondigital.bozacm.repository.EvaluationRepo;
import com.logondigital.bozacm.repository.TrajetRepo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationRepo evaluationRepo;
    private final TrajetRepo trajetRepo;

    public EvaluationServiceImpl(EvaluationRepo evaluationRepo, TrajetRepo trajetRepo) {
        this.evaluationRepo = evaluationRepo;
        this.trajetRepo = trajetRepo;
    }

    @Override
    public void createEvaluation(EvaluationReq req) {

        Trajet trajet = trajetRepo.findById(req.getTrajetId())
                .orElseThrow(() -> new ResourceNotFoundException("Trajet introuvable"));

        Evaluation evaluation = new Evaluation();
        evaluation.setNote(req.getNote());
        evaluation.setCommentaire(req.getCommentaire());
        evaluation.setDateEvaluation(new Date());
        evaluation.setTrajet(trajet);

        evaluationRepo.save(evaluation);
    }

    @Override
    public List<EvaluationResp> getAllEvaluations() {
        return evaluationRepo.findAll()
                .stream()
                .map(evaluation -> new EvaluationResp(
                        evaluation.getIdEvaluation(),
                        evaluation.getNote(),
                        evaluation.getCommentaire()
                ))
                .toList();
    }

    @Override
    public EvaluationResp getEvaluationById(Integer id) {
        Evaluation evaluation = evaluationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation introuvable"));

        return new EvaluationResp(
                evaluation.getIdEvaluation(),
                evaluation.getNote(),
                evaluation.getCommentaire()
        );
    }


    @Override
    public List<EvaluationResp> getEvaluationsByTrajet(Integer trajetId) {

        Trajet trajet = trajetRepo.findById(trajetId)
                .orElseThrow(() -> new ResourceNotFoundException("Trajet introuvable"));

        List<Evaluation> evaluations = evaluationRepo.findByTrajet(trajet);

        return evaluations.stream()
                .map(evaluation -> new EvaluationResp(
                        evaluation.getIdEvaluation(),
                        evaluation.getNote(),
                        evaluation.getCommentaire()
                ))
                .toList();
    }


    @Override
    public void updateEvaluation(Integer id, EvaluationReq evaluationReq) {

        Evaluation oldEvaluation = evaluationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation introuvable"));

        Evaluation updateTo = new Evaluation();
        updateTo.setNote(evaluationReq.getNote());
        updateTo.setCommentaire(evaluationReq.getCommentaire());

        if (evaluationReq.getTrajetId() != null) {
            Trajet trajet = trajetRepo.findById(evaluationReq.getTrajetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Trajet introuvable"));
            updateTo.setTrajet(trajet);
        }

        if (updateTo.getNote() != null)
            oldEvaluation.setNote(updateTo.getNote());

        if (updateTo.getCommentaire() != null)
            oldEvaluation.setCommentaire(updateTo.getCommentaire());

        if (updateTo.getTrajet() != null)
            oldEvaluation.setTrajet(updateTo.getTrajet());

        evaluationRepo.save(oldEvaluation);
    }
    @Override
    public void deleteEvaluation(Integer id) {
        evaluationRepo.deleteById(id);
    }
}