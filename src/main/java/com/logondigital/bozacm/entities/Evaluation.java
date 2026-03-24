package com.logondigital.bozacm.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "evaluations")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEvaluation;

    private Integer note;

    private String commentaire;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date dateEvaluation;

    @ManyToOne
    @JoinColumn(name = "trajet_id")
    private Trajet trajet;

    public Integer getIdEvaluation() {
        return idEvaluation;
    }

    public void setId(Integer idEvaluation) {
        this.idEvaluation = idEvaluation;
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

    public Date getDateEvaluation() {
        return dateEvaluation;
    }

    public void setDateEvaluation(Date dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }
}