package com.logondigital.bozacm.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "billets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Billet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBillet;
    private String numeroBillet;
    private  String qrcodeUrl;
    private LocalDateTime dateEmission;
}
