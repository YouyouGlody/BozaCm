package com.logondigital.bozacm.DTO;

import jakarta.validation.constraints.NotEmpty;

public class OffreReq {


    @NotEmpty(message = "name is required")
    private String nomOffre;


    public OffreReq(String nomOffre) {
        this.nomOffre = nomOffre;
    }
    public OffreReq() {}

    public static Integer getOffreId() {
        return 0;
    }

    public String getNomOffre() {
        return nomOffre;
    }
    public void setNomOffre(String nomOffre) {}
}
