package com.logondigital.bozacm.DTO;

import com.logondigital.bozacm.enums.TypeTransport;

public class TrajetSearchDTO {

    private String villeDepart;
    private String villeArrivee;
    private String paysDepart;
    private String paysArrivee;
    private Integer dureeMax;
    private Double distanceMax;
    private TypeTransport typeTransport;

    public TrajetSearchDTO() {}

    public String getVilleDepart() { return villeDepart; }
    public void setVilleDepart(String villeDepart) { this.villeDepart = villeDepart; }

    public String getVilleArrivee() { return villeArrivee; }
    public void setVilleArrivee(String villeArrivee) { this.villeArrivee = villeArrivee; }

    public String getPaysDepart() { return paysDepart; }
    public void setPaysDepart(String paysDepart) { this.paysDepart = paysDepart; }

    public String getPaysArrivee() { return paysArrivee; }
    public void setPaysArrivee(String paysArrivee) { this.paysArrivee = paysArrivee; }

    public Integer getDureeMax() { return dureeMax; }
    public void setDureeMax(Integer dureeMax) { this.dureeMax = dureeMax; }

    public Double getDistanceMax() { return distanceMax; }
    public void setDistanceMax(Double distanceMax) { this.distanceMax = distanceMax; }

    public TypeTransport getTypeTransport() { return typeTransport; }
    public void setTypeTransport(TypeTransport typeTransport) { this.typeTransport = typeTransport; }
}
