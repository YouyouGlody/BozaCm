package com.logondigital.bozacm.service.etape;


import com.logondigital.bozacm.entities.Etape;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface EtapeService {
    void createEtape(com.logondigital.bozacm.entities.@Valid Etape etape);
    List<Etape> getEtapes();
    Etape getEtapeById(Integer id);
    void updateEtape(Integer id, Etape etape);
    void deleteEtape(Integer id);
    @Nullable Etape getEtapeByNomEtape(String nomEtape);
}
