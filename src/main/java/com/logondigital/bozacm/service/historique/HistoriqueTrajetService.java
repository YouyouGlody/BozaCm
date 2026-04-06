package com.logondigital.bozacm.service.historique;


import com.logondigital.bozacm.DTO.HistoriqueTrajetDto;
import com.logondigital.bozacm.entities.Trajet;

import java.util.List;

public interface HistoriqueTrajetService {

    void addToHistorique(Trajet trajet);

    List<HistoriqueTrajetDto> getHistorique();

    void clearHistorique();
}
