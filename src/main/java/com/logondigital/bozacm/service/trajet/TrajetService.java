package com.logondigital.bozacm.service.trajet;



import com.logondigital.bozacm.DTO.TrajetReq;
import com.logondigital.bozacm.DTO.TrajetRespDto;
import com.logondigital.bozacm.entities.Trajet;

import java.util.List;

public interface TrajetService {
    void createTrajet(TrajetReq trajetReq);
    List<TrajetRespDto> getTrajets();
    TrajetRespDto getTrajetById(Integer idTrajet);
    void updateTrajet(Integer idTrajet, Trajet trajet);
    void deleteTrajet(Integer idTrajet);
}


