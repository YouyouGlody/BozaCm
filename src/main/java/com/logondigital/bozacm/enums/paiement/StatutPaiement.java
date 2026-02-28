package com.logondigital.bozacm.enums.paiement;

/**
 * Cycle de vie d'un paiement dans la simulation BozaCm.
 *
 * EN_ATTENTE  → paiement initié, en attente de traitement
 * EN_COURS    → en cours de traitement par le FakePaymentGateway
 * REUSSI      → paiement validé → réservation confirmée automatiquement
 * ECHOUE      → paiement refusé (carte invalide, solde insuffisant simulé…)
 * REMBOURSE   → paiement remboursé suite à une annulation
 * EXPIRE      → délai de 15 min dépassé sans validation
 */
public enum StatutPaiement {
    EN_ATTENTE,
    EN_COURS,
    REUSSI,
    ECHOUE,
    REMBOURSE,
    EXPIRE
}