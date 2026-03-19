package com.logondigital.bozacm.DTO;

import jakarta.validation.constraints.NotEmpty;

public class OffreReq {


    @NotEmpty(message = "name is required")
    private String nomOffre;
    private static Integer offreId;


    public OffreReq(String nomOffre,  Integer offreId) {
        this.nomOffre = nomOffre;
        this.offreId = offreId;
    }
    public OffreReq() {}

    public Integer getOffreId() {
        return offreId;
    }

    public String getNomOffre() {
        return nomOffre;
    }
    public void setNomOffre(String nomOffre) {this.nomOffre = nomOffre;}

    public void setOffreId(Integer offreId) {
        this.offreId = offreId;
    }
}
