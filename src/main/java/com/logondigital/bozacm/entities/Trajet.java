package com.logondigital.bozacm.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTrajet;

    private String villeDepart;
    private String villeArrivee;
    private String paysDepart;
    private String paysArrivee;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateHeureDepart;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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

    @JsonIgnore
    @OneToMany(mappedBy = "trajet", fetch = FetchType.LAZY)
    private List<Evaluation> evaluations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "trajet")
    private List<Etape> etapes = new ArrayList<>();

    // ✅ Historique
    private LocalDateTime derniereConsultation;

    public Trajet() {}

    public Trajet(
            Integer idTrajet,
            String villeDepart,
            String villeArrivee,
            String paysDepart,
            String paysArrivee,
            LocalDateTime dateHeureDepart,
            LocalDateTime dateHeureArrivee,
            Integer duree,
            Double distance,
            TypeTransport typeTransport,
            String numVol_bus,
            String nomCompagnie,
            Integer ordreTrajet,
            StatutTrajet statut,
            String description,
            Date dateCreation,
            Date dateModification,
            List<Etape> etapes
    ) {
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

    public Trajet(String villeDepart, String villeArrivee, String paysDepart, String paysArrivee, Integer duree, double distance) {
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.paysDepart = paysDepart;
        this.paysArrivee = paysArrivee;
        this.duree = duree;
        this.distance = distance;
    }

    // getters & setters (inchangés)
}
}