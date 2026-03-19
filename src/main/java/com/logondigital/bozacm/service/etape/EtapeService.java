package com.logondigital.bozacm.service.etape;

import com.example.gestion_trajets.entities.Etape;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface EtapeService {
    void createEtape(Etape etape);
    List<Etape> getEtapes();
    Etape getEtapeById(Integer id);
    void updateEtape(Integer id, Etape etape);
    void deleteEtape(Integer id);
    @Nullable Etape getEtapeByNomEtape(String nomEtape);
}
