package com.logondigital.bozacm.DTO;


import com.logondigital.bozacm.enums.TypeEtape;

public class EtapeResp {
    private Integer id;
    private String nomEtape;
    private String ville;
    private String pays;
    private Integer dureeArret;
    private TypeEtape typeEtape;
    private Integer ordre;
    private TrajetRespDto trajet;

    public EtapeResp(Integer id, String nomEtape, String ville, String pays, Integer dureeArret, TypeEtape typeEtape, Integer ordre) {
        this.id = id;
        this.nomEtape = nomEtape;
        this.ville = ville;
        this.pays = pays;
        this.dureeArret = dureeArret;
        this.typeEtape = typeEtape;
        this.ordre = ordre;
    }

    public EtapeResp() {}

    public EtapeResp(Integer id, String nomEtape, Integer dureeArret) {
        this.id = id;
        this.nomEtape = nomEtape;
        this.dureeArret = dureeArret;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
}
