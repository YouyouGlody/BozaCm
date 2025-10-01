package com.logondigital.bozacm.service.Offre;

import com.logondigital.bozacm.entities.Offre;

import java.util.List;

public interface OffreService {




        String createOffre(Offre offre);

        List<Offre> getOffres();

        Offre getOffreById(Integer offreId);

        String updateOffre(Integer offreId, Offre offre);

        String deleteOffre(Integer offreId);

        void CreateOffre(Offre offre);
    }

