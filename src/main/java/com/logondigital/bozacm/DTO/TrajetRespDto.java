package com.logondigital.bozacm.DTO;


import com.example.gestion_trajets.enums.TypeTransport;


public class TrajetRespDto {

    private Integer idTrajet;
    private String villeDepart;
    private String villeArrivee;
    private String paysDepart;
    private String paysArrivee;
    private Integer duree;
    private double distance;
    private TypeTransport typeTransport;
    private String numVol_bus;
    private String nomCompagnie;
    private Integer ordreTrajet;
    private OffreResp offreResp;

    public TrajetRespDto(Integer idTrajet, String villeDepart, String villeArrivee, String paysDepart, String paysArrivee, Integer duree, double distance, TypeTransport typeTransport, String numVol_bus, String nomCompagnie, Integer ordreTrajet) {
        this.idTrajet = idTrajet;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.paysDepart = paysDepart;
        this.paysArrivee = paysArrivee;
        this.duree = duree;
        this.distance = distance;
        this.typeTransport = typeTransport;
        this.numVol_bus = numVol_bus;
        this.nomCompagnie = nomCompagnie;
        this.ordreTrajet = ordreTrajet;

    }

    public TrajetRespDto() {}

    public TrajetRespDto(Integer idTrajet, String villeDepart, String villeArrivee, String paysDepart, String paysArrivee, Integer duree, Double distance, TypeTransport typeTransport, String numVolBus, String nomCompagnie, Integer ordreTrajet, OffreResp offreResp) {
        this.idTrajet = idTrajet;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.paysDepart = paysDepart;
        this.paysArrivee = paysArrivee;
        this.duree = duree;
        this.distance = distance;
        this.typeTransport = typeTransport;
        this.numVol_bus = numVolBus;
        this.nomCompagnie = nomCompagnie;
        this.ordreTrajet = ordreTrajet;
        this.offreResp = offreResp;

    }

    public Integer getIdTrajet() {
        return idTrajet;
    }

    public void setIdTrajet(Integer idTrajet) {
        this.idTrajet = idTrajet;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }

    public void setVilleArrivee(String villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    public String getPaysDepart() {
        return paysDepart;
    }

    public void setPaysDepart(String paysDepart) {
        this.paysDepart = paysDepart;
    }

    public String getPaysArrivee() {
        return paysArrivee;
    }

    public void setPaysArrivee(String paysArrivee) {
        this.paysArrivee = paysArrivee;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public TypeTransport getTypeTransport() {
        return typeTransport;
    }

    public void setTypeTransport(TypeTransport typeTransport) {
        this.typeTransport = typeTransport;
    }

    public String getNumVol_bus() {
        return numVol_bus;
    }

    public void setNumVol_bus(String numVol_bus) {
        this.numVol_bus = numVol_bus;
    }

    public String getNomCompagnie() {
        return nomCompagnie;
    }

    public void setNomCompagnie(String nomCompagnie) {
        this.nomCompagnie = nomCompagnie;
    }

    public Integer getOrdreTrajet() {
        return ordreTrajet;
    }

    public void setOrdreTrajet(Integer ordreTrajet) {
        this.ordreTrajet = ordreTrajet;
    }

    public OffreResp getOffreResp() {
        return offreResp;
    }

    public void setOffreResp(OffreResp offreResp) {
        this.offreResp = offreResp;
    }
}



