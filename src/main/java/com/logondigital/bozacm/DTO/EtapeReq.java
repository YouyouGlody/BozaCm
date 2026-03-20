package com.logondigital.bozacm.DTO;


import com.logondigital.bozacm.enums.TypeEtape;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class EtapeReq {
    @NotEmpty(message = "name is required")
    private String nomEtape;
    private String ville;
    @NotEmpty(message = "name is required")
    private String pays;
    private Integer dureeArret;
    @NotNull(message = "Le type d'étape est obligatoire")
    private TypeEtape typeEtape;
    private Integer ordre;
    private Integer idTrajet;


    public EtapeReq(TypeEtape typeEtape, Integer dureeArret, String pays, String ville, String nomEtape, Integer ordre, Date dateCreation, Date dateModification, Integer idTrajet) {
        this.typeEtape = typeEtape;
        this.dureeArret = dureeArret;
        this.pays = pays;
        this.ville = ville;
        this.nomEtape = nomEtape;
        this.ordre = ordre;
        this.idTrajet = idTrajet;
    }

    public EtapeReq() {}

    public String getNomEtape() {
        return nomEtape;
    }

    public void setNomEtape(String nomEtape) {
        this.nomEtape = nomEtape;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public Integer getDureeArret() {
        return dureeArret;
    }

    public void setDureeArret(Integer dureeArret) {
        this.dureeArret = dureeArret;
    }

    public TypeEtape getTypeEtape() {
        return typeEtape;
    }

    public void setTypeEtape(TypeEtape typeEtape) {
        this.typeEtape = typeEtape;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public Integer getIdTrajet() {return idTrajet;}

    public void setIdTrajet(Integer idTrajet) {this.idTrajet = idTrajet;}
}
