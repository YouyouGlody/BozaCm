package com.logondigital.bozacm.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logondigital.bozacm.enums.StatutTrajet;
import com.logondigital.bozacm.enums.TypeTransport;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "trajets")
public class Trajet {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer idTrajet;
    private String villeDepart;
    private String villeArrivee;
    private String paysDepart;
    private String paysArrivee;
    private LocalDateTime dateHeureDepart;
    private LocalDateTime dateHeureArrivee;
    private Integer duree;
    @Positive
    private Double distance;
    @Enumerated(EnumType.STRING)
    private TypeTransport typeTransport;
    private String numVol_bus;
    private String nomCompagnie;
    private Integer ordreTrajet;
    @Enumerated(EnumType.STRING)
    private StatutTrajet statut;
    private String description;
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    @Temporal(TemporalType.DATE)
    private Date dateModification;
    @OneToMany(mappedBy = "trajet", fetch = FetchType.LAZY)
    private List<Evaluation> evaluations = new ArrayList<>();
    @OneToMany(mappedBy = "trajet")
    @JsonIgnore
    private List<Etape> etapes= new ArrayList<>();


    public Trajet(Integer idTrajet, String villeDepart, String villeArrivee, String paysDepart, String paysArrivee, LocalDateTime dateHeureDepart, LocalDateTime dateHeureArrivee, Integer duree, Double distance, TypeTransport typeTransport, String numVol_bus, String nomCompagnie, Integer ordreTrajet, StatutTrajet statut, String description, Date dateCreation, Date dateModification,  List<Etape> etapes) {
        this.idTrajet = idTrajet;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.paysDepart = paysDepart;
        this.paysArrivee = paysArrivee;
        this.dateHeureDepart = dateHeureDepart;
        this.dateHeureArrivee = dateHeureArrivee;
        this.duree = duree;
        this.distance = distance;
        this.typeTransport = typeTransport;
        this.numVol_bus = numVol_bus;
        this.nomCompagnie = nomCompagnie;
        this.ordreTrajet = ordreTrajet;
        this.statut = statut;
        this.description = description;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;

        this.etapes = etapes;
    }

    public Trajet() {}

    public Trajet(String villeDepart, String villeArrivee, String paysDepart, String paysArrivee, Integer duree, double distance) {
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.paysDepart = paysDepart;
        this.paysArrivee = paysArrivee;
        this.duree = duree;
        this.distance = distance;
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

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
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

    public StatutTrajet getStatut() {
        return statut;
    }

    public void setStatut(StatutTrajet statut) {
        this.statut = statut;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }


    public List<Evaluation> getEvaluations() {
        return evaluations;
    }
    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;}

        public List<Etape> getEtapes() {
        return etapes;
    }

    public void setEtapes(List<Etape> etapes) {
        this.etapes = etapes;
    }

    }