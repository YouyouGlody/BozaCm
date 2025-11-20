package com.logondigital.bozacm.dto;

import lombok.Data;

@Data

public class TrajetRequestDTO {
    private String villeDepart;
    private String villeArrivee;
    private String duree;

    public TrajetRequestDTO() {
    }

    public TrajetRequestDTO(String villeDepart, String villeArrivee, String duree) {
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.duree = duree;
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