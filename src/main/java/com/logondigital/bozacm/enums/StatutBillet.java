package com.logondigital.bozacm.enums;

/**
 * Énumération représentant les différents statuts possibles d'un billet.

 * Le cycle de vie d'un billet :
 * 1. VALIDE → Le billet est émis et peut être utilisé
 * 2. UTILISE → Le client a effectué son voyage
 * 3. EXPIRE → Le billet n'est plus valide (date dépassée)
 * 4. ANNULE → Le billet a été annulé (réservation annulée)
 */
public enum StatutBillet {

    /**
     * Le billet est valide et peut être utilisé pour le voyage.
     * C'est le statut par défaut lors de l'émission du billet.
     */
    VALIDE,

    /**
     * Le billet a été utilisé, le voyage a été effectué.
     * Le client a scanné son QR code à l'embarquement.
     */
    UTILISE,

    /**
     * Le billet a expiré, la date de validité est dépassée.
     * Le client ne peut plus l'utiliser.
     */
    EXPIRE,

    /**
     * Le billet a été annulé (réservation annulée ou problème).
     * Il n'est plus valide.
     */
    ANNULE
}
