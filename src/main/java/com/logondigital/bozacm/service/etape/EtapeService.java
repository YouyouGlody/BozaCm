package com.logondigital.bozacm.service.etape;



import com.logondigital.bozacm.DTO.EtapeReq;
import com.logondigital.bozacm.DTO.EtapeResp;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface EtapeService {
    void createEtape(@Valid EtapeReq etapeReq);
    List<EtapeResp> getEtapes();
    EtapeResp getEtapeById(Integer id);
    void updateEtape(Integer id, EtapeReq etapeReq);
    void deleteEtape(Integer id);
    @Nullable EtapeResp getEtapeByNomEtape(String nomEtape);
}
