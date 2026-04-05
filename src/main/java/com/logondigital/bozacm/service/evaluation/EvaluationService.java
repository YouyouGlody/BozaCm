package com.logondigital.bozacm.service.evaluation;

import com.logondigital.bozacm.DTO.EvaluationReq;
import com.logondigital.bozacm.DTO.EvaluationResp;
import com.logondigital.bozacm.DTO.PageResp;

import java.util.List;

public interface EvaluationService {

    void createEvaluation(EvaluationReq evaluationReq);

    List<EvaluationResp> getAllEvaluations();

    EvaluationResp getEvaluationById(Integer idEvaluation);

    void deleteEvaluation(Integer idEvaluation);

    List<EvaluationResp> getEvaluationsByTrajet(Integer trajetId);

    void updateEvaluation(Integer idEvaluation, EvaluationReq evaluationReq);

    PageResp<EvaluationResp> getAllEvaluationsPaginated(int page, int size);

    List<EvaluationResp> getByNote(Integer note);

    

}