package com.logondigital.bozacm.service.trajet;

import com.logondigital.bozacm.DTO.*;
import com.logondigital.bozacm.enums.TypeTransport;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

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

List<TrajetRespDto> getMultiCritere(
        String villeDepart,
        String villeArrivee,
        String paysDepart,
        String paysArrivee,
        Integer dureeMax,
        Double distanceMax,
        TypeTransport typeTransport
);

TrajetMtclDTO extractMtcl(Integer idTrajet);

List<TrajetRespDto> getHistoriqueTrajets();