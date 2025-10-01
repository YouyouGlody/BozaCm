package com.logondigital.bozacm.entities;



import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
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
}

