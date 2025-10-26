package com.logondigital.bozacm.dto;

import lombok.Data;

import java.util.Date;

@Data

public class ReservationRequestDTO {
    private String nomClient;
    private String emailClient;
    private Date dateReservation;
    private String statut;
    private Integer offreId;

    public ReservationRequestDTO() {
    }

    public ReservationRequestDTO(String nomClient, String emailClient, Date dateReservation, String statut, Integer offreId) {
        this.nomClient = nomClient;
        this.emailClient = emailClient;
        this.dateReservation = dateReservation;
        this.statut = statut;
        this.offreId = offreId;
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

    public Integer getOffreId() {
        return offreId;
    }

    public void setOffreId(Integer offreId) {
        this.offreId = offreId;
    }
}
