package com.logondigital.bozacm.dto;

import lombok.Data;

import java.util.Date;

@Data

public class OffreRequestDTO {
    private String titre;
    private String description;
    private Double prix;
    private Date dateDepart;
    private Integer agenceId;
    private Integer trajetId;

    public OffreRequestDTO() {
    }

    public OffreRequestDTO(String titre, String description, Double prix, Date dateDepart, Integer agenceId, Integer trajetId) {
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.dateDepart = dateDepart;
        this.agenceId = agenceId;
        this.trajetId = trajetId;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Integer getAgenceId() {
        return agenceId;
    }

    public void setAgenceId(Integer agenceId) {
        this.agenceId = agenceId;
    }

    public Integer getTrajetId() {
        return trajetId;
    }

    public void setTrajetId(Integer trajetId) {
        this.trajetId = trajetId;
    }
}