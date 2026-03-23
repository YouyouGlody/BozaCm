package com.logondigital.bozacm.DTO;

public class EvaluationReq {

    private Integer note;
    private String commentaire;
    private Integer trajetId;

    public EvaluationReq(Integer note, String commentaire, Integer trajetId) {
        this.note = note;
        this.commentaire = commentaire;
        this.trajetId = trajetId;
    }

    public EvaluationReq() {
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Integer getTrajetId() {
        return trajetId;
    }

    public void setTrajetId(Integer trajetId) {
        this.trajetId = trajetId;
    }
}