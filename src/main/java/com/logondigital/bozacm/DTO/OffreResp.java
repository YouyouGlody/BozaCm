package com.logondigital.bozacm.DTO;

public class OffreResp {
    private Integer id;
    private String nom;

    public OffreResp(Integer id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public OffreResp() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
