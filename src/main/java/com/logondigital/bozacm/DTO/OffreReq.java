package com.logondigital.bozacm.DTO;

import jakarta.validation.constraints.NotEmpty;

public class OffreReq {


    @NotEmpty(message = "name is required")
    private String nom;


    public OffreReq(String nom) {
        this.nom = nom;
    }
    public OffreReq() {}

    public static Integer getOffreId() {
        return 0;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {}
}
