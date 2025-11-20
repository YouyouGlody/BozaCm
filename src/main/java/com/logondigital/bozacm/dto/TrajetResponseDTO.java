package com.logondigital.bozacm.dto;

import lombok.Data;

@Data

public class TrajetResponseDTO {
    private Integer id;
    private String villeDepart;
    private String villeArrivee;
    private String duree;

    public TrajetResponseDTO() {
    }

    public TrajetResponseDTO(Integer id, String villeDepart, String villeArrivee, String duree) {
        this.id = id;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.duree = duree;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDuree() {
        return duree;
    }

    public void setDistance(String duree) {
        this.duree = duree;
    }
}