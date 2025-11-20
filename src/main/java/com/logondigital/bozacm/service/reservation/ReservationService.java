package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.dto.PageResponseDTO;
import com.logondigital.bozacm.dto.ReservationRequestDTO;
import com.logondigital.bozacm.dto.ReservationResponseDTO;

import java.util.List;

public interface ReservationService {
    void createReservation(ReservationRequestDTO dto);

    List<ReservationResponseDTO> getAllReservations();

    ReservationResponseDTO getReservationById(Integer id);

    void updateReservation(Integer id, ReservationRequestDTO dto);

    void deleteReservation(Integer id);

    PageResponseDTO<ReservationResponseDTO> getAllReservationsPaginated(int page, int size, String sortBy);

    List<ReservationResponseDTO> getReservationsByStatut(String statut);
    List<ReservationResponseDTO> getReservationsByClient(String email);
}

