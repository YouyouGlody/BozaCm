package com.logondigital.bozacm.service.reservation;

import com.logondigital.bozacm.dto.ReservationRequestDTO;
import com.logondigital.bozacm.dto.ReservationResponseDTO;
import com.logondigital.bozacm.entities.Reservation;

import java.util.List;

public interface ReservationService {
    void createReservation(ReservationRequestDTO dto);

    List<ReservationResponseDTO> getAllReservations();

    ReservationResponseDTO getReservationById(Integer id);

    void updateReservation(Integer id, ReservationRequestDTO dto);

    void deleteReservation(Integer id);


}

