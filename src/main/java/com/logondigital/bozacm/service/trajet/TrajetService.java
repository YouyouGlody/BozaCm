package com.logondigital.bozacm.service.trajet;



import com.logondigital.bozacm.DTO.PageResp;
import com.logondigital.bozacm.DTO.TrajetReq;
import com.logondigital.bozacm.DTO.TrajetRespDto;
import com.logondigital.bozacm.enums.TypeTransport;
import jakarta.validation.Valid;

import java.util.List;

public interface TrajetService {
    void createTrajet(TrajetReq trajetReq);
    List<TrajetRespDto> getTrajets();
    TrajetRespDto getTrajetById(Integer idTrajet);
    void updateTrajet(Integer idTrajet, @Valid TrajetReq trajet);
    void deleteTrajet(Integer idTrajet);
    PageResp<TrajetRespDto> getAllTrajetsPaginated(int page, int size, String sortBy);
    List<TrajetRespDto> getByPaysDepart(String pays);
    List<TrajetRespDto> getByPaysArrivee(String pays);
    List<TrajetRespDto> getByTypeTransport(TypeTransport type);
    List<TrajetRespDto> getByRoute(String depart, String arrivee);
}


