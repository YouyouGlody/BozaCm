package com.logondigital.bozacm.entities;



import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "trajets")
public class Trajet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String depart;
    private String arrivee;
    private String duree;
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @OneToMany(mappedBy = "trajet")
    private List<Offre> offres = new ArrayList<>();


    public String getName() {
        return depart;
    }

    public void setName(String name) {
        this.depart= name;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;


    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    public Trajet() {
    }

    public Trajet(Integer id, String depart, String arrivee, String duree, Date createdAt, Date updatedAt, List<Offre> offres) {
        this.id = id;
        this.depart = depart;
        this.arrivee = arrivee;
        this.duree = duree;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.offres = offres;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getArrivee() {
        return arrivee;
    }

    public void setArrivee(String arrivee) {
        this.arrivee = arrivee;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public List<Offre> getOffres() {
        return offres;
    }

    public void setOffres(List<Offre> offres) {
        this.offres = offres;
    }
}





