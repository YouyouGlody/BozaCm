package com.logondigital.bozacm.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Table(name = "reservations")
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nomClient;
    private String emailClient;

    @Temporal(TemporalType.DATE)
    private Date dateReservation;
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    private String statut;


    @ManyToOne
    @JsonIgnoreProperties("reservations")
    private Offre offre;

    public String getName() {
        return nomClient;
    }

    public void setName(String name) {
        this.nomClient= name;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;


    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    public Reservation() {
    }

    public Reservation(Integer id, String nomClient, String emailClient, Date dateReservation, Date createdAt, Date updatedAt, String statut, Offre offre) {
        this.id = id;
        this.nomClient = nomClient;
        this.emailClient = emailClient;
        this.dateReservation = dateReservation;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.statut = statut;
        this.offre = offre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public Date getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Offre getOffre() {
        return offre;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;
    }
}



