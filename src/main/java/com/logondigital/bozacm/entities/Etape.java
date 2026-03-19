package com.logondigital.bozacm.entities;

import com.example.gestion_trajets.enums.TypeEtape;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "etapes")
public class Etape {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 3, max = 6, message = "Min 3 et Max 6")
    private String nomEtape;

    private String ville;
    private String pays;

    private LocalDateTime heureDepartEtape;
    private LocalDateTime heureArriveEtape;

    private Integer dureeArret;

    @Enumerated(EnumType.STRING)
    private TypeEtape typeEtape;

    private Integer ordre;
    private String description;

    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    @Temporal(TemporalType.DATE)
    private Date dateModification;

    @ManyToOne
    @JoinColumn(name = "trajet_id")
    private Trajet trajet;

    public Etape() {}

    public Etape(Integer id, String nomEtape, String ville, String pays,
                 LocalDateTime heureDepartEtape, LocalDateTime heureArriveEtape,
                 Integer dureeArret, TypeEtape typeEtape, Integer ordre,
                 String description, Date dateCreation, Date dateModification, Trajet trajet) {
        this.id = id;
        this.nomEtape = nomEtape;
        this.ville = ville;
        this.pays = pays;
        this.heureDepartEtape = heureDepartEtape;
        this.heureArriveEtape = heureArriveEtape;
        this.dureeArret = dureeArret;
        this.typeEtape = typeEtape;
        this.ordre = ordre;
        this.description = description;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.trajet = trajet;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomEtape() {
        return nomEtape;
    }

    public void setNomEtape(String nomEtape) {
        this.nomEtape = nomEtape;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public LocalDateTime getHeureDepartEtape() {
        return heureDepartEtape;
    }

    public void setHeureDepartEtape(LocalDateTime heureDepartEtape) {
        this.heureDepartEtape = heureDepartEtape;
    }

    public LocalDateTime getHeureArriveEtape() {
        return heureArriveEtape;
    }

    public void setHeureArriveEtape(LocalDateTime heureArriveEtape) {
        this.heureArriveEtape = heureArriveEtape;
    }

    public Integer getDureeArret() {
        return dureeArret;
    }

    public void setDureeArret(Integer dureeArret) {
        this.dureeArret = dureeArret;
    }

    public TypeEtape getTypeEtape() {
        return typeEtape;
    }

    public void setTypeEtape(TypeEtape typeEtape) {
        this.typeEtape = typeEtape;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
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
    public Trajet getTrajet() {
        return trajet;
    }
    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }
}
