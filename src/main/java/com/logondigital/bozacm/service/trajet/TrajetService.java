package com.logondigital.bozacm.service.trajet;

import com.logondigital.bozacm.dto.PageResponseDTO;
import com.logondigital.bozacm.dto.TrajetRequestDTO;
import com.logondigital.bozacm.dto.TrajetResponseDTO;

import java.util.List;

public interface TrajetService {

    TrajetResponseDTO createTrajet(TrajetRequestDTO dto);

    List<TrajetResponseDTO> getAllTrajets();

    PageResponseDTO<TrajetResponseDTO> getAllTrajetsPaginated(int page, int size, String sortBy);

    TrajetResponseDTO getTrajetById(Integer id);

    TrajetResponseDTO updateTrajet(Integer id, TrajetRequestDTO dto);

    void deleteTrajet(Integer id);

    List<TrajetResponseDTO> getTrajetsByDepart(String depart);

    List<TrajetResponseDTO> getTrajetsByRoute(String depart, String arrivee);
    List<TrajetResponseDTO> rechercher(String terme);
}