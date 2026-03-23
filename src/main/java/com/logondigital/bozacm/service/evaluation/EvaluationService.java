package com.logondigital.bozacm.service.evaluation;

import com.logondigital.bozacm.DTO.EvaluationReq;
import com.logondigital.bozacm.DTO.EvaluationResp;

import java.util.List;

public interface EvaluationService {

    void createEvaluation(EvaluationReq req);

    List<EvaluationResp> getAllEvaluations();

    EvaluationResp getEvaluationById(Integer id);

    void deleteEvaluation(Integer id);

    List<EvaluationResp> getEvaluationsByTrajet(Integer trajetId);

    void updateEvaluation(Integer id, EvaluationReq req);
}