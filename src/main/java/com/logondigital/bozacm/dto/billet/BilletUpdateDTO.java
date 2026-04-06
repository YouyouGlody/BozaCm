package com.logondigital.bozacm.dto.billet;

import com.logondigital.bozacm.enums.StatutBillet;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * DTO pour modifier un billet existant.
 * Utilisé lors de la requête PUT /api/v1/billets/{id}

 * IMPORTANT : Seuls certains champs peuvent être modifiés !

 * CHAMPS MODIFIABLES :
 * - statutBillet (pour marquer comme UTILISE, EXPIRE, ANNULE)
 * - qrcodeUrl (si régénération du QR Code)

 * CHAMPS NON-MODIFIABLES (pour garantir l'intégrité) :
 * - numeroBillet (unique et immuable)
 * - dateEmission (historique figé)
 * - dateExpiration (calculée automatiquement)
 * - nomClientSurBillet / prenomClientSurBillet (snapshot figé)
 * - client (relation figée)
 * - reservation (relation figée)

 * Note : Tous les champs sont optionnels.
 * Seuls les champs fournis (non-null) seront mis à jour.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BilletUpdateDTO {

    // ==================== CHAMPS MODIFIABLES ====================

    /**
     * Nouveau statut du billet (optionnel)

     * Valeurs autorisées :
     * - VALIDE : Billet valide et utilisable
     * - UTILISE : Billet utilisé (après embarquement)
     * - EXPIRE : Billet expiré (date dépassée)
     * - ANNULE : Billet annulé (réservation annulée)

     * Cas d'usage :
     * - Agent marque le billet comme UTILISE après scan QR Code
     * - Système marque automatiquement comme EXPIRE après dateExpiration
     * - Annulation de réservation → Billet passe à ANNULE
     */
    private StatutBillet statutBillet;

    /**
     * Nouvelle URL du QR Code (optionnel)

     * Cas d'usage :
     * - Régénération du QR Code (suite à un problème technique)
     * - Migration vers un nouveau système de QR Code

     * Format attendu : URL complète ou chemin relatif
     * Exemples :
     * - "https://api.bozacm.com/qrcodes/BZC-xxx.png"
     * - "/static/qrcodes/BZC-xxx.png"
     */
    @Pattern(
            regexp = "^(https?://.*|/.*)?$",
            message = "L'URL du QR Code doit être une URL valide ou un chemin relatif"
    )
    private String qrcodeUrl;

    // ==================== MÉTHODES UTILITAIRES ====================

    /**
     * Vérifie si au moins un champ est fourni pour la mise à jour
     *
     * @return true si au moins un champ non-null, false sinon
     */
    public boolean hasAnyField() {
        return statutBillet != null || qrcodeUrl != null;
    }

    /**
     * Compte le nombre de champs fournis
     *
     * @return nombre de champs non-null
     */
    public int countProvidedFields() {
        int count = 0;
        if (statutBillet != null) count++;
        if (qrcodeUrl != null) count++;
        return count;
    }

    /**
     * Vérifie si seul le statut est modifié
     * Utile pour différencier les cas d'usage
     *
     * @return true si seul statutBillet est fourni
     */
    public boolean isOnlyStatutUpdate() {
        return statutBillet != null && qrcodeUrl == null;
    }

    /**
     * Vérifie si seul le QR Code est modifié
     *
     * @return true si seul qrcodeUrl est fourni
     */
    public boolean isOnlyQrcodeUpdate() {
        return qrcodeUrl != null && statutBillet == null;
    }
}