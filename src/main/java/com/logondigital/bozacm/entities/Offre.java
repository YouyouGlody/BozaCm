package com.logondigital.bozacm.entities;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "offres")
public class Offre {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titre;
    private String description;
    private Double prix;
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @Temporal(TemporalType.DATE)
    private Date dateDepart;

    @ManyToOne
    @JsonIgnoreProperties({"offres"})  // ignore la liste d'offres dans Agence
    private Agence agence;
    @OneToMany(mappedBy = "offre")
    private List<Reservation> reservations = new ArrayList<>();
    @ManyToOne
    @JsonIgnoreProperties({"offres"})  // ignore la liste d'offres dans Trajet
    private Trajet trajet;

    public String getName() {
        return titre;
    }

    public void setName(String name) {
        this.titre= name;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;


    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Offre() {
    }

    public Offre(Integer id, String titre, String description, Double prix, Date createdAt, Date updatedAt, Date dateDepart, Agence agence, List<Reservation> reservations, Trajet trajet) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.dateDepart = dateDepart;
        this.agence = agence;
        this.reservations = reservations;
        this.trajet = trajet;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }



    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }
}

