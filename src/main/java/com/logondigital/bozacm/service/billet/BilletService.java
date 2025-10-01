package com.logondigital.bozacm.service.billet;

import com.logondigital.bozacm.entities.Billet;

import java.util.List;

public interface BilletService {

    void createBillet(Billet billet);

    List<Billet> getAllBillets();

    Billet getBilletById(Integer idBillet);

    void updateBillet(Billet billet);

    void deleteBilletById(Integer idBillet);

    Billet getBilletByNumeroBillet(String numeroBillet);


}
