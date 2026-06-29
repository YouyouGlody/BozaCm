package com.logondigital.bozacm.service.billet;

import com.logondigital.bozacm.entities.Billet;
import com.logondigital.bozacm.enums.StatutBillet;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.BilletRepo;
import com.logondigital.bozacm.service.qrcode.QRCodeService;
import com.logondigital.bozacm.service.pdf.PdfService;
import com.logondigital.bozacm.service.email.EmailService;
import com.logondigital.bozacm.DTO.billet.BilletResponseDTO;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implémentation du service BilletService.
 * AVEC GÉNÉRATION AUTOMATIQUE DE QR CODE + EMAIL !
 */
@Service
public class BilletServiceImp implements BilletService {

    private static final Logger logger = LoggerFactory.getLogger(BilletServiceImp.class);

    private final BilletRepo billetRepo;
    private final QRCodeService qrCodeService;
    private final PdfService pdfService;
    private final EmailService emailService;

    // ========== CONSTRUCTEUR ==========
    public BilletServiceImp(
            BilletRepo billetRepo,
            QRCodeService qrCodeService,
            PdfService pdfService,
            EmailService emailService
    ) {
        this.billetRepo = billetRepo;
        this.qrCodeService = qrCodeService;
        this.pdfService = pdfService;
        this.emailService = emailService;
    }

    // ========== CRÉER UN BILLET AVEC QR CODE + EMAIL ==========
    @Override
    public Billet createBillet(Billet billet) {
        logger.info("🎫 Création d'un nouveau billet...");

        Billet billetSauvegarde = billetRepo.save(billet);
        logger.info("✅ Billet sauvegardé (ID: {})", billetSauvegarde.getIdBillet());
        logger.info("   Numéro : {}", billetSauvegarde.getNumeroBillet());

        try {
            logger.info("🔲 Génération du QR Code...");
            String qrcodeUrl = qrCodeService.generateQRCode(
                    billetSauvegarde.getNumeroBillet()
            );
            logger.info("✅ QR Code généré : {}", qrcodeUrl);

            billetSauvegarde.setQrcodeUrl(qrcodeUrl);
            billetSauvegarde = billetRepo.save(billetSauvegarde);
            logger.info("✅ URL du QR Code enregistrée");

        } catch (Exception e) {
            logger.error("❌ Erreur génération QR Code : {}", e.getMessage());
            logger.error("   Le billet est créé sans QR Code");
        }

        try {
            logger.info("📧 Préparation de l'envoi de l'email...");

            BilletResponseDTO billetResponse = convertirEnDTO(billetSauvegarde);

            byte[] pdfBytes = pdfService.generateBilletPdf(billetResponse);
            logger.info("✅ PDF généré ({} bytes)", pdfBytes.length);

            String emailClient = billetSauvegarde.getClient().getEmail();

            emailService.envoyerBilletParEmail(
                    billetResponse,
                    emailClient,
                    pdfBytes,
                    billetSauvegarde.getQrcodeUrl()
            );

            logger.info("✅ Email envoyé avec succès à {}", emailClient);
            logger.info("   → PDF attaché : Billet_{}.pdf", billetResponse.getNumeroBillet());
            logger.info("   → QR Code intégré : {}", billetSauvegarde.getQrcodeUrl());

        } catch (Exception e) {
            logger.error("❌ ERREUR lors de l'envoi de l'email : {}", e.getMessage());
            logger.error("   Le billet a quand même été créé avec succès");
            logger.error("   Le client peut récupérer son billet via l'API");
            e.printStackTrace();
        }

        logger.info("🎉 Billet créé avec succès !");
        return billetSauvegarde;
    }

    // ========== MÉTHODE HELPER : CONVERTIR EN DTO ==========
    private BilletResponseDTO convertirEnDTO(Billet billet) {
        return BilletResponseDTO.builder()
                .numeroBillet(billet.getNumeroBillet())
                .statutBillet(billet.getStatutBillet())
                .dateEmission(billet.getDateEmission())
                .dateExpiration(billet.getDateExpiration())
                .qrcodeUrl(billet.getQrcodeUrl())
                .clientNomComplet(billet.getClient().getNom() + " " + billet.getClient().getPrenom())
                .clientEmail(billet.getClient().getEmail())
                .clientTelephone(billet.getClient().getNumeroTelephone())
                .villeDeDepart(getVilleDepart(billet))
                .villeArrivee(getVilleArrivee(billet))
                .dateDepart(getDateDepart(billet))
                .prixReservation(getPrixReservation(billet))
                .compagnieBus(getCompagnieBus(billet))
                .compagnieAerienne(getCompagnieAerienne(billet))
                .compagnieTrain(getCompagnieTrain(billet))
                .build();
    }

    // ========== MÉTHODES HELPER (lecture via l'offre) ==========
    private String getVilleDepart(Billet billet) {
        try {
            if (billet.getReservation() != null) {
                return billet.getReservation().getOffre().getTrajet().getDepart();
            }
        } catch (Exception e) {
            logger.warn("Impossible de récupérer la ville de départ : {}", e.getMessage());
        }
        return "N/A";
    }

    private String getVilleArrivee(Billet billet) {
        try {
            if (billet.getReservation() != null) {
                return billet.getReservation().getOffre().getTrajet().getArrivee();
            }
        } catch (Exception e) {
            logger.warn("Impossible de récupérer la ville d'arrivée : {}", e.getMessage());
        }
        return "N/A";
    }

    private LocalDateTime getDateDepart(Billet billet) {
        try {
            if (billet.getReservation() != null && billet.getReservation().getOffre().getDateDepart() != null) {
                return billet.getReservation().getOffre().getDateDepart().atStartOfDay();
            }
        } catch (Exception e) {
            logger.warn("Impossible de récupérer la date de départ : {}", e.getMessage());
        }
        return LocalDateTime.now();
    }

    private Double getPrixReservation(Billet billet) {
        try {
            if (billet.getReservation() != null) {
                return billet.getReservation().getOffre().getPrix();
            }
        } catch (Exception e) {
            logger.warn("Impossible de récupérer le prix : {}", e.getMessage());
        }
        return 0.0;
    }

    private String getCompagnieBus(Billet billet) {
        try {
            if (billet.getReservation() != null) {
                String className = billet.getReservation().getClass().getSimpleName();
                if (className.contains("Bus")) {
                    java.lang.reflect.Method method = billet.getReservation().getClass().getMethod("getCompagnieBus");
                    return (String) method.invoke(billet.getReservation());
                }
            }
        } catch (Exception e) {
            logger.debug("Pas une réservation de bus");
        }
        return null;
    }

    private String getCompagnieAerienne(Billet billet) {
        try {
            if (billet.getReservation() != null) {
                String className = billet.getReservation().getClass().getSimpleName();
                if (className.contains("Avion")) {
                    java.lang.reflect.Method method = billet.getReservation().getClass().getMethod("getCompagnieAerienne");
                    return (String) method.invoke(billet.getReservation());
                }
            }
        } catch (Exception e) {
            logger.debug("Pas une réservation d'avion");
        }
        return null;
    }

    private String getCompagnieTrain(Billet billet) {
        try {
            if (billet.getReservation() != null) {
                String className = billet.getReservation().getClass().getSimpleName();
                if (className.contains("Train")) {
                    java.lang.reflect.Method method = billet.getReservation().getClass().getMethod("getCompagnieTrain");
                    return (String) method.invoke(billet.getReservation());
                }
            }
        } catch (Exception e) {
            logger.debug("Pas une réservation de train");
        }
        return null;
    }

    // ========== RESTE DU CODE INCHANGÉ ==========
    @Override
    public List<Billet> getAllBillets() {
        return billetRepo.findAll();
    }

    @Override
    public Billet getBilletById(Integer idBillet) {
        return billetRepo.findById(idBillet).orElseThrow(
                () -> new RessourceNotFoundException("Billet non trouvé avec l'ID: " + idBillet)
        );
    }

    @Override
    public void deleteBilletById(Integer idBillet) {
        logger.info("🗑️ Suppression du billet ID : {}", idBillet);
        Billet billet = getBilletById(idBillet);

        if (billet.getQrcodeUrl() != null && !billet.getQrcodeUrl().isEmpty()) {
            logger.info("🗑️ Suppression du QR Code...");
            qrCodeService.deleteQRCode(billet.getQrcodeUrl());
        }

        billetRepo.deleteById(idBillet);
        logger.info("✅ Billet supprimé");
    }

    @Override
    public long countBillets() {
        return billetRepo.count();
    }

    @Override
    public Billet findByNumeroBillet(String numeroBillet) {
        return billetRepo.findByNumeroBillet(numeroBillet)
                .orElseThrow(
                        () -> new RessourceNotFoundException("Billet non trouvé avec le numéro: " + numeroBillet)
                );
    }

    @Override
    public List<Billet> getBilletsClient(Integer clientId) {
        return billetRepo.findByClientIdClientOrderByDateEmissionDesc(clientId);
    }

    @Override
    public List<Billet> getBilletsParStatut(Integer clientId, StatutBillet statut) {
        return billetRepo.findByClientIdClientAndStatutBillet(clientId, statut);
    }

    @Override
    public Billet getBilletByReservation(Integer reservationId) {
        return billetRepo.findByReservationIdReservation(reservationId)
                .orElseThrow(
                        () -> new RessourceNotFoundException("Billet non trouvé pour la réservation: " + reservationId)
                );
    }

    @Override
    public long countBilletsByClient(Integer clientId) {
        return billetRepo.countByClientIdClient(clientId);
    }

    @Override
    public Billet marquerBilletUtilise(String numeroBillet) {
        Billet billet = findByNumeroBillet(numeroBillet);
        billet.setStatutBillet(StatutBillet.UTILISE);
        return billetRepo.save(billet);
    }

    @Override
    public void expirerBilletsPerimes() {
        LocalDateTime maintenant = LocalDateTime.now();
        List<Billet> billetsExpires = billetRepo.findByDateExpirationBefore(maintenant);

        for (Billet billet : billetsExpires) {
            if (billet.getStatutBillet() == StatutBillet.VALIDE) {
                billet.setStatutBillet(StatutBillet.EXPIRE);
                billetRepo.save(billet);
            }
        }
    }
}