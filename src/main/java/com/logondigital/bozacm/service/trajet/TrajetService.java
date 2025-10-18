package com.logondigital.bozacm.service.trajet;

import com.logondigital.bozacm.entities.Trajet;

import java.util.List;

public interface TrajetService {
    void createTrajet(Trajet trajet);

    List<Trajet> getTrajets();

    Trajet getTrajetById(Integer trajetId);

    void updateTrajet(Integer trajetId, Trajet trajet);

    void deleteTrajet(Integer trajetId);


}
