package com.logondigital.bozacm.service.trajet;

import com.example.gestion_trajets.DTO.TrajetReq;
import com.example.gestion_trajets.DTO.TrajetRespDto;
import com.example.gestion_trajets.entities.Trajet;

import java.util.List;

public interface TrajetService {
    void createTrajet(TrajetReq trajet);
    List<Trajet> getTrajets();
    TrajetRespDto getTrajetById(Integer idTrajet);
    void updateTrajet(Integer idTrajet, Trajet trajet);
    void deleteTrajet(Integer idTrajet);
}
