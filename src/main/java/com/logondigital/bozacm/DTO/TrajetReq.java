package com.logondigital.bozacm.DTO;


import com.logondigital.bozacm.enums.TypeTransport;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class TrajetReq {
    private String nomOffre;
    @Valid
    private  OffreReq offreReq;
    private Integer idTrajet;
    private Integer offreId;
    private String nom;
    private String villeDepart;
    @NotEmpty
    private String villeArrivee;
    private String paysDepart;
    private String paysArrivee;
    private Integer duree;
    private double distance;
    private LocalDateTime dateHeureDepart;
    private LocalDateTime dateHeureArrivee;
    @NotNull(message = "Le type de transport est obligatoire")
    private TypeTransport typeTransport;
    private String numVol_bus;
    @NotEmpty(message = "name is required")
    private String nomCompagnie;
    private Integer ordreTrajet;

    public TrajetReq(String nom, String villeDepart, String villeArrivee, String paysDepart, String paysArrivee, Integer duree, double distance, LocalDate dateHeureDepart, LocalDate dateHeureArrivee, TypeTransport typeTransport, String numVol_bus, String nomCompagnie, Integer ordreTrajet, Date dateCreation, Date dateModification, OffreReq offreReq) {
        this.nom = nom;
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
        this.offreReq = offreReq;
        this.dateHeureArrivee = dateHeureArrivee.atStartOfDay();
        this.dateHeureDepart = dateHeureDepart.atStartOfDay();
}

    public TrajetReq() {
    }

    public TrajetReq(OffreReq offreReq) {
        this.offreReq = offreReq;
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

    public void setIdTrajet(Integer IdTrajet) {
    }

    public void setDateCreation(Date date) {
    }

    public void setDateModification(Date date) {
    }

    public String getNom() {
        return nom;
    }

    public OffreReq getOffreReq() {
        return offreReq;
    }

    public void setOffreReq(OffreReq offreReq) {
        this.offreReq = offreReq;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDateTime getDateHeureDepart() {
        return dateHeureDepart;
    }

    public void setDateHeureDepart(LocalDateTime dateHeureDepart) {
        this.dateHeureDepart = dateHeureDepart;
    }

    public LocalDateTime getDateHeureArrivee() {
        return dateHeureArrivee;
    }

    public void setDateHeureArrivee(LocalDateTime dateHeureArrivee) {
        this.dateHeureArrivee = dateHeureArrivee;
    }

    public Integer getOffreId() {
        return offreId;
    }
    public void setOffreId(Integer offreId) { this.offreId = offreId; }

    public Integer getIdTrajet() {
    return idTrajet;}
}
