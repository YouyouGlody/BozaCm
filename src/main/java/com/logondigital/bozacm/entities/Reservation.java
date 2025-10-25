package com.logondigital.bozacm.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.util.Date;


@Entity
@Table(name = "reservations")
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Le nom du client est obligatoire")
    private String nomClient;

    @Email(message = "L'email du client doit être valide")
    @NotBlank(message = "L'email du client est obligatoire")
    private String emailClient;

    @Temporal(TemporalType.DATE)
    @NotNull(message = "La date de réservation est obligatoire")
    private Date dateReservation;
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @Temporal(TemporalType.DATE)
    private Date updatedAt;
    @NotBlank(message = "Le statut est obligatoire")
    private String statut;


    @ManyToOne
    @JsonIgnoreProperties("reservations")
    @NotNull(message = "La réservation doit être liée à une offre")
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



