package com.logondigital.bozacm.service.trajet;

import com.logondigital.bozacm.DTO.*;
import com.logondigital.bozacm.entities.Evaluation;
import com.logondigital.bozacm.entities.Trajet;
import com.logondigital.bozacm.enums.TypeTransport;
import com.logondigital.bozacm.exception.ResourceNotFoundException;
import com.logondigital.bozacm.repository.EtapeRepo;
import com.logondigital.bozacm.repository.EvaluationRepo;
import com.logondigital.bozacm.repository.TrajetRepo;
import com.logondigital.bozacm.service.historique.HistoriqueTrajetService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TrajetServiceImpl implements TrajetService {

    private final TrajetRepo trajetRepo;
    private final EvaluationRepo evaluationRepo;
    private final EtapeRepo etapeRepo;
    private final HistoriqueTrajetService historiqueTrajetService;

    public TrajetServiceImpl(
            TrajetRepo trajetRepo,
            EvaluationRepo evaluationRepo,
            EtapeRepo etapeRepo,
            HistoriqueTrajetService historiqueTrajetService
    ) {
        this.trajetRepo = trajetRepo;
        this.evaluationRepo = evaluationRepo;
        this.etapeRepo = etapeRepo;
        this.historiqueTrajetService = historiqueTrajetService;
    }

    // ✅ le reste du code trajet_vianey inchangé
}
                content,
                trajetPage.getNumber(),
                trajetPage.getSize(),
                trajetPage.getTotalElements(),
                trajetPage.getTotalPages(),
                trajetPage.isLast()
        );
return dto;
}

@Override
public List<TrajetRespDto> getHistoriqueTrajets() {
    return trajetRepo.getHistorique()
            .stream()
            .map(this::toDTO)
            .toList();
}
}
                .map(this::toDTO)
                .toList();
    }

}