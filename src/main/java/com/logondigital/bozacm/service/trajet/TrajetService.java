package com.logondigital.bozacm.service.trajet;

import com.logondigital.bozacm.entities.Trajet;

import java.util.List;

public interface TrajetService {
    void createTrajet(Trajet trajet);

    List<Trajet> getTrajets();

    Trajet getTrajetById(Integer trajetId);

    String updateTrajet(Integer trajetId, Trajet trajet);

    String deleteTrajet(Integer trajetId);


}
