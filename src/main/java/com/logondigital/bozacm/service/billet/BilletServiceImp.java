package com.logondigital.bozacm.service.billet;

import com.logondigital.bozacm.entities.Billet;
import com.logondigital.bozacm.enums.StatutBillet;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.BilletRepo;
import com.logondigital.bozacm.service.qrcode.QRCodeService;  // ← NOUVEAU IMPORT
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implémentation du service BilletService.
 * AVEC GÉNÉRATION AUTOMATIQUE DE QR CODE !
 */
@Service
public class BilletServiceImp implements BilletService {

    private final BilletRepo billetRepo;
    private final QRCodeService qrCodeService;  // ← NOUVEAU CHAMP

    // ========== CONSTRUCTEUR ==========
    public BilletServiceImp(BilletRepo billetRepo, QRCodeService qrCodeService) {
        this.billetRepo = billetRepo;
        this.qrCodeService = qrCodeService;  // ← INJECTION
    }

    // ========== CRÉER UN BILLET AVEC QR CODE ==========
    @Override
    public Billet createBillet(Billet billet) {
        System.out.println(" Création d'un nouveau billet...");

        // ========== 1. PREMIÈRE SAUVEGARDE ==========
        // Cela génère le numeroBillet via @PrePersist
        Billet billetSauvegarde = billetRepo.save(billet);

        System.out.println(" Billet sauvegardé (ID: " + billetSauvegarde.getIdBillet() + ")");
        System.out.println("   Numéro : " + billetSauvegarde.getNumeroBillet());

        // ========== 2. GÉNÉRER LE QR CODE ==========
        try {
            System.out.println(" Génération du QR Code...");

            String qrcodeUrl = qrCodeService.generateQRCode(
                    billetSauvegarde.getNumeroBillet()
            );

            System.out.println(" QR Code généré : " + qrcodeUrl);

            // ========== 3. DEUXIÈME SAUVEGARDE ==========
            // Ajouter l'URL du QR Code
            billetSauvegarde.setQrcodeUrl(qrcodeUrl);
            billetSauvegarde = billetRepo.save(billetSauvegarde);

            System.out.println(" URL du QR Code enregistrée");

        } catch (Exception e) {
            // Si le QR Code échoue, le billet est quand même créé
            System.err.println(" Erreur génération QR Code : " + e.getMessage());
            System.err.println("   Le billet est créé sans QR Code");
        }

        System.out.println("Billet créé avec succès !");
        return billetSauvegarde;
    }

    // ========== RÉCUPÉRER TOUS LES BILLETS ==========
    @Override
    public List<Billet> getAllBillets() {
        return billetRepo.findAll();
    }

    // ========== RÉCUPÉRER UN BILLET PAR ID ==========
    @Override
    public Billet getBilletById(Integer idBillet) {
        return billetRepo.findById(idBillet).orElseThrow(
                () -> new RessourceNotFoundException("Billet non trouvé avec l'ID: " + idBillet)
        );
    }

    // ========== SUPPRIMER UN BILLET ET SON QR CODE ==========
    @Override
    public void deleteBilletById(Integer idBillet) {
        System.out.println(" Suppression du billet ID : " + idBillet);

        // Récupérer le billet
        Billet billet = getBilletById(idBillet);

        System.out.println("   Numéro : " + billet.getNumeroBillet());
        System.out.println("   QR Code : " + billet.getQrcodeUrl());

        // Supprimer le QR Code du disque
        if (billet.getQrcodeUrl() != null && !billet.getQrcodeUrl().isEmpty()) {
            System.out.println(" Suppression du QR Code...");
            boolean deleted = qrCodeService.deleteQRCode(billet.getQrcodeUrl());
            if (deleted) {
                System.out.println("QR Code supprimé");
            }
        }

        // Supprimer le billet de la base
        billetRepo.deleteById(idBillet);
        System.out.println("Billet supprimé");
    }

    // ========== COMPTER LES BILLETS ==========
    @Override
    public long countBillets() {
        return billetRepo.count();
    }

    // ========== TROUVER PAR NUMÉRO ==========
    @Override
    public Billet findByNumeroBillet(String numeroBillet) {
        return billetRepo.findByNumeroBillet(numeroBillet)
                .orElseThrow(
                        () -> new RessourceNotFoundException("Billet non trouvé avec le numéro: " + numeroBillet)
                );
    }

    // ========== BILLETS D'UN CLIENT ==========
    @Override
    public List<Billet> getBilletsClient(Integer clientId) {
        return billetRepo.findByClientIdClientOrderByDateEmissionDesc(clientId);
    }

    // ========== BILLETS PAR STATUT ==========
    @Override
    public List<Billet> getBilletsParStatut(Integer clientId, StatutBillet statut) {
        return billetRepo.findByClientIdClientAndStatutBillet(clientId, statut);
    }

    // ========== BILLET PAR RÉSERVATION ==========
    @Override
    public Billet getBilletByReservation(Integer reservationId) {
        return billetRepo.findByReservationIdReservation(reservationId)
                .orElseThrow(
                        () -> new RessourceNotFoundException("Billet non trouvé pour la réservation: " + reservationId)
                );
    }

    // ========== COMPTER BILLETS CLIENT ==========
    @Override
    public long countBilletsByClient(Integer clientId) {
        return billetRepo.countByClientIdClient(clientId);
    }

    // ========== MARQUER COMME UTILISÉ ==========
    @Override
    public Billet marquerBilletUtilise(String numeroBillet) {
        Billet billet = findByNumeroBillet(numeroBillet);
        billet.setStatutBillet(StatutBillet.UTILISE);
        return billetRepo.save(billet);
    }

    // ========== EXPIRER LES BILLETS PÉRIMÉS ==========
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