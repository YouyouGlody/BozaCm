package com.logondigital.bozacm.enums.paiement;

/**
 * Modes de paiement disponibles dans la simulation BozaCm.
 *
 * ⚠️ SIMULATION UNIQUEMENT — Aucun vrai argent n'est débité.
 * Tous les paiements sont traités par le FakePaymentGateway interne.
 *
 * En production, chaque mode correspondrait à une vraie API :
 *  - CARTE_BANCAIRE  → Stripe API
 *  - PAYPAL          → PayPal REST API
 *  - VIREMENT        → Virement bancaire manuel
 *  - ESPECES_AGENCE  → Paiement physique en agence
 */
public enum ModePaiement {

    /** Simulation carte Visa/Mastercard (en production : Stripe) */
    CARTE_BANCAIRE,

    /** Simulation PayPal (en production : PayPal REST API) */
    PAYPAL,

    /** Virement bancaire simulé */
    VIREMENT_BANCAIRE,

    /** Paiement en espèces à l'agence (confirmation manuelle par l'agent) */
    ESPECES_AGENCE
}