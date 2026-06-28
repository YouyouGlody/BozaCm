package com.logondigital.bozacm.service.trajet;

import com.logondigital.bozacm.DTO.PageResponseDTO;
import com.logondigital.bozacm.DTO.TrajetRequestDTO;
import com.logondigital.bozacm.DTO.TrajetResponseDTO;
import com.logondigital.bozacm.DTO.PageResponseDTO;
import com.logondigital.bozacm.DTO.TrajetRequestDTO;
import com.logondigital.bozacm.DTO.TrajetResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface TrajetService {

    TrajetResponseDTO createTrajet(@Valid TrajetRequestDTO dto);

    List<TrajetResponseDTO> getAllTrajets();

    PageResponseDTO<TrajetResponseDTO> getAllTrajetsPaginated(int page, int size, String sortBy);

    TrajetResponseDTO getTrajetById(Integer id);

    TrajetResponseDTO updateTrajet(Integer id, TrajetRequestDTO dto);

    void deleteTrajet(Integer id);

    List<TrajetResponseDTO> getTrajetsByDepart(String depart);

    List<TrajetResponseDTO> getTrajetsByRoute(String depart, String arrivee);
    List<TrajetResponseDTO> rechercher(String terme);
}