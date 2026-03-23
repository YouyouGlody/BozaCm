package com.logondigital.bozacm.DTO;

public class EvaluationResp {

    private Integer idEvaluation;
    private Integer note;
    private String commentaire;

    public EvaluationResp(Integer idEvaluation, Integer note, String commentaire) {
        this.idEvaluation = idEvaluation;
        this.note = note;
        this.commentaire = commentaire;
    }

    public Integer getIdEvaluation() {
        return idEvaluation;
    }

    public Integer getNote() {
        return note;
    }

    public String getCommentaire() {
        return commentaire;
    }
}