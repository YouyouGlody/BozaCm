package com.logondigital.bozacm.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class EvaluationReq {

    @NotBlank(message = "une note est obligatoire")
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