package com.logondigital.bozacm.service.trajet;

import com.logondigital.bozacm.dto.TrajetRequestDTO;
import com.logondigital.bozacm.dto.TrajetResponseDTO;

import java.util.List;

public interface TrajetService {
    void createTrajet(TrajetRequestDTO dto);

    List<TrajetResponseDTO> getAllTrajets();

    TrajetResponseDTO getTrajetById(Integer id);

    void updateTrajet(Integer id, TrajetRequestDTO dto);

    void deleteTrajet(Integer id);

    List<TrajetResponseDTO> getTrajetsByDepart(String depart);
    List<TrajetResponseDTO> getTrajetsByRoute(String depart, String arrivee);
}
